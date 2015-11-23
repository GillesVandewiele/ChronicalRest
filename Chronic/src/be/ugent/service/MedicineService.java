package be.ugent.service;

import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.google.gson.Gson;

import be.ugent.dao.MedicineDao;
import be.ugent.entitity.Medicine;
import be.ugent.entitity.Medicine;
import be.ugent.entitity.Patient;

@Path("/MedicineService")
public class MedicineService {
	MedicineDao medicineDao = new MedicineDao();
	Gson genson = new Gson();

	
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
		System.out.println("Got request to add medicine: "+genson.toJson(medicine));
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
		
		
		System.out.println("Created medicine: "+genson.toJson(toAdd));
		
		if(medicineDao.changeMedicine(toAdd)){
			//return medicine successfully created
			return Response.status(202).entity(medicineDao.getMedicine(toAdd.getMedicineID(),toAdd.getPatientID())).build();
		}else{
			//return record was already in database, or was wrong format
			return Response.status(409).build();
		}
	}

	
}
