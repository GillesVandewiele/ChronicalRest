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
import be.ugent.entitity.Drug;

@Path("/DrugService")
public class DrugService {
	DrugDao drugDao = new DrugDao();
	Genson genson = new Genson();

	
	@GET
	@Path("/drugs")
	@Produces({ MediaType.APPLICATION_JSON })
	public List<Drug> getAllDrugs(@Context HttpHeaders header, @Context HttpServletResponse response) {
		response.addHeader("Access-Control-Allow-Origin", "*");
		return drugDao.getAllDrugs();

	}

	@POST
	@Path("/drugs")
	@Consumes({MediaType.APPLICATION_JSON})
	public Response addUser(Drug drug){
		System.out.println("Got request to add user: "+genson.serialize(drug));
		
		Drug toAdd = new Drug();
		
		toAdd.setDescription(drug.getDescription());
		toAdd.setName(drug.getName());
		toAdd.setDrugID(drugDao.getNewDrugID());
		
		
		System.out.println("Created User: "+genson.serialize(toAdd));
		if(drugDao.addDrug(toAdd)){
			//return drug successfully created
			return Response.status(201).build();
		}else{
			//return record was already in database, or was wrong format
			return Response.status(409).build();
		}
		
		
		
	}

	
}
