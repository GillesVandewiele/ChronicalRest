package be.ugent.service;

import javax.ws.rs.Consumes;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.google.gson.Gson;

import be.ugent.Authentication;
import be.ugent.dao.MedicineDao;
import be.ugent.entitity.Medicine;

@Path("/MedicineService")
public class MedicineService {
	MedicineDao medicineDao = new MedicineDao();
	Gson gson = new Gson();

	
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
		if(medicineDao.getMedicineID(medicine)<0){
			return Response.status(404).build();
		}
		toAdd.setMedicineID(medicineDao.getMedicineID(medicine));
		
		
		System.out.println("Created medicine: "+gson.toJson(toAdd));
		
		if(medicineDao.changeMedicine(toAdd)){
			//return medicine successfully created
			return Response.status(202).entity(medicineDao.getMedicine(toAdd.getMedicineID(),toAdd.getPatientID())).build();
		}else{
			//return record was already in database, or was wrong format
			return Response.status(409).build();
		}
	}
	
	@POST
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
