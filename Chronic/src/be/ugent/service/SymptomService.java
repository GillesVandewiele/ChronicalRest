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

import com.owlike.genson.Genson;

import be.ugent.dao.DrugDao;
import be.ugent.dao.SymptomDao;
import be.ugent.entitity.Drug;
import be.ugent.entitity.Symptom;

@Path("/SymptomService")
public class SymptomService {
	SymptomDao symptomDao = new SymptomDao();
	Genson genson = new Genson();

	
	@GET
	@Path("/Symptoms")
	@Produces({ MediaType.APPLICATION_JSON })
	public List<Symptom> getAllsymptoms(@Context HttpHeaders header, @Context HttpServletResponse response) {
		response.addHeader("Access-Control-Allow-Origin", "*");
		return symptomDao.getAllSymptoms();

	}

	@POST
	@Path("/Symptoms")
	@Consumes({MediaType.APPLICATION_JSON})
	public Response addUser(Symptom symptom){
		System.out.println("Got request to add symptom: "+genson.serialize(symptom));
		
		Symptom toAdd = new Symptom();
		
		toAdd.setDescription(symptom.getDescription());
		toAdd.setName(symptom.getName());
		toAdd.setSymptomID(symptomDao.getNewSymptomID());
		
		
		System.out.println("Created User: "+genson.serialize(toAdd));
		if(symptomDao.addSymptom(toAdd)){
			//return symptom successfully created
			return Response.status(201).build();
		}else{
			//return record was already in database, or was wrong format
			return Response.status(409).build();
		}
		
		
		
	}

	
}
