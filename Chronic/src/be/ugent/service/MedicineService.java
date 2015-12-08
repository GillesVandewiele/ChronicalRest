package be.ugent.service;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.google.gson.Gson;

import be.ugent.Authentication;
import be.ugent.dao.MedicineDao;
import be.ugent.dao.PatientDao;
import be.ugent.entitity.Drug;
import be.ugent.entitity.Headache;
import be.ugent.entitity.Medicine;
import be.ugent.entitity.Patient;

@Path("/MedicineService")
public class MedicineService {
	MedicineDao medicineDao = new MedicineDao();
	PatientDao patientDao = new PatientDao();
	Gson gson = new Gson();

	@POST
	@Path("/medicines")
	@Consumes({MediaType.APPLICATION_JSON})
	public Response addMedicine(String medicine, @HeaderParam("Authorization") String header, @QueryParam("patientID") String patientID) {
		
//		System.out.println("header:"+header);
		if(!Authentication.isAuthorized(header)){
			return Response.status(403).build();
		}
		
		if(medicine == null || patientID==null || patientID.isEmpty()){
			return Response.status(422).build();
		}
		JSONObject medicineJSON = null;
		try {
			medicineJSON = new JSONObject(medicine);
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		System.out.println("object:"+medicineJSON);
		Medicine toAdd = new Medicine();
		try {
			toAdd.setDate(medicineJSON.getString("date"));
			toAdd.setDrugID(medicineJSON.getInt("drugID"));
			toAdd.setQuantity(medicineJSON.getInt("quantity"));			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		System.out.println("Got request to add medicine: "+toAdd);
		
		
		toAdd.setMedicineID(medicineDao.getNewMedicineID());
		
		
//		System.out.println("Created medicine: "+JSON.parse(toAdd.toJSON().toString()));
		
		//TODO return object with correct ID (now id will not be updated in the return object
		toAdd.setPatientID(Integer.parseInt(patientID));
		if(medicineDao.addMedicine(toAdd)){
			//return medicine successfully created
			
			try {
				return Response.status(201).entity(medicineDao.getMedicine(medicineJSON.get("date")+"")).build();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return Response.status(500).build();
			}
		}else{
//			return record was already in database, or was wrong format
			return Response.status(409).build();
		}
	}
	
	@POST
	@Path("/medicines/update")
	@Consumes({MediaType.APPLICATION_JSON})
	public Response changeMedicine(Medicine medicine) {
//		if(!Authentication.isAuthorized(header)){
//			return Response.status(403).build();
//		}		
		
		Medicine toAdd = medicine;
		
		if(toAdd == null){
			return Response.status(422).build();
		}
		System.out.println("Got request to add medicine: "+gson.toJson(medicine));
		//if it's a medicine that is not yet submitted to the database
		if(medicine.getMedicineID()==-1){
			int id = medicineDao.getNewMedicineID();
			medicine.setMedicineID(id);
			if(medicineDao.addMedicine(medicine)){
				return Response.status(201).entity(medicineDao.getMedicine(toAdd.getMedicineID(),toAdd.getPatientID())).build();
			}else{
				//medicine given is already in database, but with wrong medicineID
				return Response.status(409).build();
			}
		}
		
		System.out.println("Changing medicine: "+gson.toJson(toAdd));
		
		if(medicineDao.changeMedicine(toAdd)){
			//return medicine successfully created
			return Response.status(202).entity(medicineDao.getMedicine(toAdd.getMedicineID(),toAdd.getPatientID())).build();
		}else{
			//return record was already in database, or was wrong format
			return Response.status(409).build();
		}
	}
	
	@DELETE
	@Path("/medicines/delete")
	@Consumes({MediaType.APPLICATION_JSON})
	public Response deleteMedicine(Medicine medicine, @HeaderParam("Authorization") String header){
		if(!Authentication.isAuthorized(header)){
			return Response.status(403).build();
		}				
		
		Medicine toAdd = medicine;
		
		if(toAdd == null){
			return Response.status(422).build();
		}
		System.out.println("Got request to delete medicine: "+gson.toJson(medicine));
		//if it's a medicine that is not yet submitted to the database
		if(medicine.getMedicineID()<0){
			//medicine given is already in database, but with wrong medicineID
			return Response.status(404).build();
			
		}
		
		if(medicineDao.deleteMedicine(toAdd)){
			//return medicine successfully deleted
			return Response.status(200).build();
		}else{
			//return record was already in database, or was wrong format
			return Response.status(404).build();
		}
	}

	
}
