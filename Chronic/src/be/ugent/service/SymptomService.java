package be.ugent.service;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.google.gson.Gson;

import be.ugent.Authentication;
import be.ugent.dao.SymptomDao;
import be.ugent.entitity.Symptom;

@Path("/SymptomService")
public class SymptomService {
	SymptomDao symptomDao = new SymptomDao();
	Gson genson = new Gson();
	
	@GET
	@Path("/symptoms")
	@Produces({ MediaType.APPLICATION_JSON })
	public Response getAllSymptoms() {
		return Response.ok(symptomDao.getAllSymptoms()).build();
	}
	

	@POST
	@Path("/symptoms")
	@Consumes({MediaType.APPLICATION_JSON})
	public Response addSymptom(Symptom symptom, @HeaderParam("Authorization") String header) {
		if(!Authentication.isAuthorized(header)){
			return Response.status(403).build();
		}		
		System.out.println("Got request to add symptom: "+genson.toJson(symptom));
		
		Symptom toAdd = symptom;
		
		if(toAdd == null){
			return Response.status(422).build();
		}
		toAdd.setSymptomID(symptomDao.getNewSymptomID());
		
		
		System.out.println("Created symptom: "+genson.toJson(toAdd));
		
		if(symptomDao.addSymptom(toAdd)){
			//return symptom successfully created
			return Response.status(201).build();
		}else{
			//return record was already in database, or was wrong format
			return Response.status(409).build();
		}
	}


	
}
