package be.ugent.service;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.google.gson.Gson;
import com.owlike.genson.Genson;

import be.ugent.Authentication;
import be.ugent.dao.PatientDao;
import be.ugent.entitity.Patient;

@Path("/PatientService")
public class PatientService {
	PatientDao patientDao = new PatientDao();
	Genson genson = new Genson();

	
	@GET
	@Path("/patients")
	@Produces({ MediaType.APPLICATION_JSON })
	public Patient getUser(@QueryParam("firstName") String firstName, @QueryParam("lastName") String lastName) {
		System.out.println("Patient opgevraagd met naam: " + firstName + " " + lastName);
		return patientDao.getPatient(firstName, lastName);

	}

	@POST
	@Path("/patients")
	@Consumes({MediaType.APPLICATION_JSON})
	public Response addUser(String pat, @Context HttpHeaders headers){
		String header = headers.getRequestHeader("Authorization").get(0);
		if(!Authentication.isAuthorized(header)){
			return Response.status(403).build();
		}
		System.out.println("Patient requested to add: "+pat.toString());
		Gson gson = new Gson();
		Patient toAdd = gson.fromJson(pat.toString(), Patient.class);
		System.out.println("Patient to add:"+toAdd);
		if(patientDao.storePatient(toAdd)){
			//return patient successfully created
			return Response.status(201).build();
		}else{
			//return record was already in database, or was wrong format
			return Response.status(409).build();
		}
	}

	
}
