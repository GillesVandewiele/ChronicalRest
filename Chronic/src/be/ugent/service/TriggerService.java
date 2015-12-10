package be.ugent.service;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.google.gson.Gson;

import be.ugent.Authentication;
import be.ugent.dao.DrugDao;
import be.ugent.dao.TriggerDao;
import be.ugent.entitity.Drug;
import be.ugent.entitity.Trigger;

@Path("/TriggerService")
public class TriggerService {
	TriggerDao triggerDao = new TriggerDao();
	Gson gson = new Gson();

	
	@GET
	@Path("/triggers")
	@Produces({ MediaType.APPLICATION_JSON })
	public Response getAllTriggers(@HeaderParam("Authorization") String header) {
		System.out.println("header:" + header);
		if (!Authentication.isAuthorized(header)) {
			return Response.status(403).build();
		}
		return Response.ok(triggerDao.getAllTriggers()).build();
	}

	@PUT
	@Path("/triggers")
	@Consumes({MediaType.APPLICATION_JSON})
	public Response addTrigger(Trigger trigger,@HeaderParam("Authorization") String header) {
		System.out.println("header:" + header);
		if (!Authentication.isAuthorized(header)) {
			return Response.status(403).build();
		}
		System.out.println("Got request to add Trigger: "+gson.toJson(trigger));
		
		Trigger toAdd = new Trigger();
		
		toAdd.setDescription(trigger.getDescription());
		toAdd.setName(trigger.getName());
		toAdd.setTriggerID(triggerDao.getNewTriggerID());
		
		
		System.out.println("Created User: "+gson.toJson(toAdd));
		if(triggerDao.addTrigger(toAdd)){
			//return Trigger successfully created
			return Response.status(201).entity(triggerDao.getTrigger(toAdd.getName())).build();
		}else{
			//return record was already in database, or was wrong format
			return Response.status(409).build();
		}
	}
	
	@POST
	@Path("/triggers/update")
	@Consumes({MediaType.APPLICATION_JSON})
	public Response changeTrigger(Trigger trigger, @HeaderParam("Authorization") String header){
		if(!Authentication.isAuthorized(header)){
			return Response.status(403).build();
		}				
		
		Trigger toAdd = trigger;
		
		if(toAdd == null){
			return Response.status(422).build();
		}
		System.out.println("Got request to add trigger: "+gson.toJson(trigger));
		//if it's a trigger that is not yet submitted to the database
		if(trigger.getTriggerID()==-1){
			int id = triggerDao.getNewTriggerID();
			trigger.setTriggerID(id);
			if(triggerDao.addTrigger(trigger)){
				return Response.status(201).entity(triggerDao.getTrigger(toAdd.getName())).build();
			}else{
				//trigger given is already in database, but with wrong triggerID
				return Response.status(409).build();
			}
		}
				
		System.out.println("Changing trigger: "+gson.toJson(toAdd));
		
		if(triggerDao.changeTrigger(toAdd)){
			//return trigger successfully created
			return Response.status(202).entity(triggerDao.getTrigger(toAdd.getName())).build();
		}else{
			//return record was already in database, or was wrong format
			return Response.status(409).build();
		}
	}
	
	@DELETE
	@Path("/triggers/delete")
	@Consumes({MediaType.APPLICATION_JSON})
	public Response deleteTrigger(Trigger trigger, @HeaderParam("Authorization") String header){
		if(!Authentication.isAuthorized(header)){
			return Response.status(403).build();
		}				
		
		Trigger toAdd = trigger;
		
		if(toAdd == null){
			return Response.status(422).build();
		}
		System.out.println("Got request to delete trigger: "+gson.toJson(trigger));
		//if it's a trigger that is not yet submitted to the database
		if(trigger.getTriggerID()<0){
			//trigger given is already in database, but with wrong triggerID
			return Response.status(404).build();
			
		}
		
		if(triggerDao.deleteTrigger(toAdd)){
			//return trigger successfully deleted
			return Response.status(200).build();
		}else{
			//return record was already in database, or was wrong format
			return Response.status(404).build();
		}
	}
	

	
}
