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
import be.ugent.dao.TriggerDao;
import be.ugent.entitity.Drug;
import be.ugent.entitity.Trigger;

@Path("/TriggerService")
public class TriggerService {
	TriggerDao TriggerDao = new TriggerDao();
	Genson genson = new Genson();

	
	@GET
	@Path("/Triggers")
	@Produces({ MediaType.APPLICATION_JSON })
	public List<Trigger> getAllTriggers(@Context HttpHeaders header, @Context HttpServletResponse response) {
		response.addHeader("Access-Control-Allow-Origin", "*");
		return TriggerDao.getAllTriggers();

	}

	@POST
	@Path("/Triggers")
	@Consumes({MediaType.APPLICATION_JSON})
	public Response addUser(Trigger trigger){
		System.out.println("Got request to add Trigger: "+genson.serialize(trigger));
		
		Trigger toAdd = new Trigger();
		
		toAdd.setDescription(trigger.getDescription());
		toAdd.setName(trigger.getName());
		toAdd.setTriggerID(TriggerDao.getNewTriggerID());
		
		
		System.out.println("Created User: "+genson.serialize(toAdd));
		if(TriggerDao.addTrigger(toAdd)){
			//return Trigger successfully created
			return Response.status(201).build();
		}else{
			//return record was already in database, or was wrong format
			return Response.status(409).build();
		}
		
		
		
	}

	
}
