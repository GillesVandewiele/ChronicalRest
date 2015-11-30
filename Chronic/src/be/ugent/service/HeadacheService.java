package be.ugent.service;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.google.gson.Gson;

import be.ugent.Authentication;
import be.ugent.dao.HeadacheDao;
import be.ugent.dao.PatientDao;
import be.ugent.entitity.Headache;
import be.ugent.entitity.Patient;

@Path("/HeadacheService")
public class HeadacheService {
	HeadacheDao headacheDao = new HeadacheDao();
	PatientDao patientDao = new PatientDao();
	Gson gson = new Gson();
	
	@GET
	@Path("/headaches")
	@Produces({ MediaType.APPLICATION_JSON })
	public Response getAllHeadaches( @QueryParam("patientID") String patientID) {
		
		return Response.ok(headacheDao.getAllHeadachesForPatient(Integer.parseInt(patientID))).build();
	}
	
	@GET
	@Path("/headaches/semantics")
	@Produces({ MediaType.TEXT_PLAIN })
	public Response getHeadacheSemantics(@HeaderParam("Authorization") String header, @QueryParam("patientID") String patientID) {
		return Response.ok(headacheDao.getSemantics("<http://localhost:8080/Chronic/rest/HeadacheService/headaches?patientID="+patientID+">")).build();
	}
	

	@PUT
	@Path("/headaches")
	@Consumes({MediaType.APPLICATION_JSON})
	public Response addHeadache(Headache headache, @HeaderParam("Authorization") String header, @QueryParam("patientID") String patientID) {
		System.out.println("header:"+header);
		if(!Authentication.isAuthorized(header)){
			return Response.status(403).build();
		}
		Headache toAdd = headache;
		
		if(toAdd == null){
			return Response.status(422).build();
		}
		
		System.out.println("Got request to add headache: "+gson.toJson(headache));
		
		
		toAdd.setHeadacheID(headacheDao.getNewHeadacheID());
		
		
		System.out.println("Created headache: "+gson.toJson(toAdd));
		
		//TODO return object with correct ID (now id will not be updated in the return object
		Patient patient = patientDao.getPatienFromId(patientID);
		if(headacheDao.addHeadacheForPatient(patient, toAdd)){
			//return headache successfully created
			return Response.status(201).entity(toAdd).build();
		}else{
			//return record was already in database, or was wrong format
			return Response.status(409).build();
		}
	}

	
	
	
	
}
