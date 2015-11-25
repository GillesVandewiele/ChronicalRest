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
import be.ugent.dao.DrugDao;
import be.ugent.entitity.Drug;

@Path("/DrugService")
public class DrugService {
	DrugDao drugDao = new DrugDao();
	Gson gson = new Gson();
	
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
		System.out.println("header:"+header);
		if(!Authentication.isAuthorized(header)){
			return Response.status(403).build();
		}
		Drug toAdd = drug;
		
		if(toAdd == null){
			return Response.status(422).build();
		}
		
		System.out.println("Got request to add drug: "+gson.toJson(drug));
		
		
		toAdd.setDrugID(drugDao.getNewDrugID());
		
		
		System.out.println("Created drug: "+gson.toJson(toAdd));
		
		if(drugDao.addDrug(toAdd)){
			//return drug successfully created
			return Response.status(201).entity(drugDao.getDrug(toAdd.getName())).build();
		}else{
			//return record was already in database, or was wrong format
			return Response.status(409).build();
		}
	}

	@POST
	@Path("/drugs/update")
	@Consumes({MediaType.APPLICATION_JSON})
	public Response changeDrug(Drug drug, @HeaderParam("Authorization") String header){
		if(!Authentication.isAuthorized(header)){
			return Response.status(403).build();
		}				
		
		Drug toAdd = drug;
		
		if(toAdd == null){
			return Response.status(422).build();
		}
		System.out.println("Got request to update drug: "+gson.toJson(drug));
		//if it's a drug that is not yet submitted to the database
		if(drug.getDrugID()==-1){
			int id = drugDao.getNewDrugID();
			drug.setDrugID(id);
			if(drugDao.addDrug(drug)){
				return Response.status(201).entity(drugDao.getDrug(toAdd.getName())).build();
			}else{
				//drug given is already in database, but with wrong drugID
				return Response.status(409).build();
			}
		}
				
		System.out.println("Created drug: "+gson.toJson(toAdd));
		
		if(drugDao.changeDrug(toAdd)){
			//return drug successfully created
			return Response.status(202).entity(drugDao.getDrug(toAdd.getName())).build();
		}else{
			//return record was already in database, or was wrong format
			return Response.status(409).build();
		}
	}
	
	@POST
	@Path("/drugs/delete")
	@Consumes({MediaType.APPLICATION_JSON})
	public Response deleteDrug(Drug drug, @HeaderParam("Authorization") String header){
		if(!Authentication.isAuthorized(header)){
			return Response.status(403).build();
		}				
		
		Drug toAdd = drug;
		
		if(toAdd == null){
			return Response.status(422).build();
		}
		System.out.println("Got request to delete drug: "+gson.toJson(drug));
		//if it's a drug that is not yet submitted to the database
		if(drug.getDrugID()<0){
			//drug given is already in database, but with wrong drugID
			return Response.status(404).build();
			
		}
		
		if(drugDao.deleteDrug(toAdd)){
			//return drug successfully deleted
			return Response.status(200).build();
		}else{
			//return record was already in database, or was wrong format
			return Response.status(404).build();
		}
	}
	
	
	
}
