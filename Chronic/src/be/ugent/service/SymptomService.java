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
import be.ugent.entitity.Drug;
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
		Symptom toAdd = symptom;
		
		if(toAdd == null){
			return Response.status(422).build();
		}
		System.out.println("Got request to add symptom: "+genson.toJson(symptom));
		
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

	@POST
	@Path("/symptoms/update")
	@Consumes({MediaType.APPLICATION_JSON})
	public Response changeSymptom(Symptom symptom) {
//		if(!Authentication.isAuthorized(header)){
//			return Response.status(403).build();
//		}		
		Symptom toAdd = symptom;
		if(toAdd == null){
			return Response.status(422).build();
		}
		System.out.println("Got request to add symptom: "+genson.toJson(symptom));
		//if it's a symptom that is not yet submitted to the database
		if(symptom.getSymptomID()==-1){
			int id = symptomDao.getNewSymptomID();
			symptom.setSymptomID(id);
			if(symptomDao.addSymptom(symptom)){
				return Response.status(201).entity(symptomDao.getSymptom(toAdd.getName())).build();
			}else{
				//symptom given is already in database, but with wrong symptomID
				return Response.status(409).build();
			}
		}
		if(symptomDao.getSymptomID(symptom)<0){
			return Response.status(404).build();
		}
		toAdd.setSymptomID(symptomDao.getSymptomID(symptom));
		
		
		System.out.println("Created symptom: "+genson.toJson(toAdd));
		
		if(symptomDao.changeSymptom(toAdd)){
			//return symptom successfully created
			return Response.status(202).entity(symptomDao.getSymptom(toAdd.getName())).build();
		}else{
			//return record was already in database, or was wrong format
			return Response.status(409).build();
		}
	}
	
	@POST
	@Path("/symptoms/delete")
	@Consumes({MediaType.APPLICATION_JSON})
	public Response deleteSymptom(Symptom symptom, @HeaderParam("Authorization") String header){
		if(!Authentication.isAuthorized(header)){
			return Response.status(403).build();
		}				
		
		Symptom toAdd = symptom;
		
		if(toAdd == null){
			return Response.status(422).build();
		}
		System.out.println("Got request to delete symptom: "+genson.toJson(symptom));
		//if it's a symptom that is not yet submitted to the database
		if(symptom.getSymptomID()<0){
			//symptom given is already in database, but with wrong symptomID
			return Response.status(404).build();
			
		}
		
		if(symptomDao.deleteSymptom(toAdd)){
			//return symptom successfully deleted
			return Response.status(200).entity(symptomDao.getSymptom(toAdd.getName())).build();
		}else{
			//return record was already in database, or was wrong format
			return Response.status(404).build();
		}
	}
	
}
