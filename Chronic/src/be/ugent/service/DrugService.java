package be.ugent.service;

import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
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
import be.ugent.dao.DrugDao;
import be.ugent.dao.PatientDao;
import be.ugent.entitity.Drug;
import be.ugent.entitity.Patient;

@Path("/DrugService")
public class DrugService {
	DrugDao drugDao = new DrugDao();
	Gson genson = new Gson();
	
	@GET
	@Path("/drugs")
	@Produces({ MediaType.APPLICATION_JSON })
	public Response getAllDrugs() {
		return Response.ok(drugDao.getAllDrugs()).build();
	}
	

	@POST
	@Path("/drugs")
	@Consumes({MediaType.APPLICATION_JSON})
	public Response addDrug(Drug drug, @HeaderParam("Authorization") String header) {
		if(!Authentication.isAuthorized(header)){
			return Response.status(403).build();
		}		
		System.out.println("Got request to add drug: "+genson.toJson(drug));
		
		Drug toAdd = drug;
		
		if(toAdd == null){
			return Response.status(422).build();
		}
		toAdd.setDrugID(drugDao.getNewDrugID());
		
		
		System.out.println("Created drug: "+genson.toJson(toAdd));
		
		if(drugDao.addDrug(toAdd)){
			//return drug successfully created
			return Response.status(201).build();
		}else{
			//return record was already in database, or was wrong format
			return Response.status(409).build();
		}
		
		
		
	}

	
}
