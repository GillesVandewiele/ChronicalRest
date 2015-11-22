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
import be.ugent.entitity.Patient;

@Path("/MedicineService")
public class MedicineService {
	MedicineDao medicineDao = new MedicineDao();
	Gson genson = new Gson();

	
//	@GET
//	@Path("/medicines")
//	@Produces({ MediaType.APPLICATION_JSON })
//	public List<Medicine> getAllMedicines(@Context HttpHeaders header, @Context HttpServletResponse response, int patientID) {
//					
//		response.addHeader("Access-Control-Allow-Origin", "*");
//		return medicineDao.getAllMedicinesForPatient();
//
//	}

//	@POST
//	@Path("/Medicines")
//	@Consumes({MediaType.APPLICATION_JSON})
//	public Response addUser(Medicine Medicine){
//		System.out.println("Got request to add user: "+genson.toJson(Medicine));
//		
//		Medicine toAdd = new Medicine();
//		
//		toAdd.setDescription(Medicine.getDescription());
//		toAdd.setName(Medicine.getName());
//		toAdd.setMedicineID(medicineDao.getNewMedicineID());
//		
//		
//		System.out.println("Created User: "+genson.toJson(toAdd));
//		if(medicineDao.addMedicine(toAdd)){
//			//return Medicine successfully created
//			return Response.status(201).build();
//		}else{
//			//return record was already in database, or was wrong format
//			return Response.status(409).build();
//		}
//		
//		
//		
//	}

	
}
