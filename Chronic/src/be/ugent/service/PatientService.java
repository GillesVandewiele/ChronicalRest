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

import com.owlike.genson.Genson;

import be.ugent.dao.PatientDao;
import be.ugent.entitity.Patient;

@Path("/PatientService")
public class PatientService {
	PatientDao patientDao = new PatientDao();
	Genson genson = new Genson();

	
	@GET
	@Path("/patients")
	@Produces({ MediaType.APPLICATION_JSON })
	public Patient getUser(@QueryParam("firstName") String firstName, @QueryParam("lastName") String lastName,
			@Context HttpHeaders header, @Context HttpServletResponse response) {
		response.addHeader("Access-Control-Allow-Origin", "*");
		System.out.println("Patient opgevraagd met naam: " + firstName + " " + lastName);
		return patientDao.getPatient(firstName, lastName);

	}

	@POST
	@Path("/patients")
	@Consumes({MediaType.APPLICATION_JSON})
	public Response addUser(Patient pat){
		System.out.println("Got request to add user: "+genson.serialize(pat));
		
		Patient toAdd = new Patient();
		
		
		toAdd.setBirthDate(pat.getBirthDate());
		toAdd.setEmployed(pat.isEmployed());
		toAdd.setFirstName(pat.getFirstName());
		toAdd.setLastName(pat.getLastName());
		toAdd.setPatientID(patientDao.getNewId());
		toAdd.setAdvice(pat.getAdvice());
		toAdd.setDiagnosis(pat.getDiagnosis());
		toAdd.setMale(pat.isMale());
		toAdd.setEmail(pat.getEmail());
		
		
		
		System.out.println("Created User: "+genson.serialize(toAdd));
		if(patientDao.storePatient(toAdd)){
			//return patient successfully created
			return Response.status(201).build();
		}else{
			//return record was already in database, or was wrong format
			return Response.status(409).build();
		}
	}

	
}
