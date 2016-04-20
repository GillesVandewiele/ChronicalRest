package be.ugent.service;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.google.gson.Gson;

import be.ugent.Authentication;
//import be.ugent.dao.MachineLearningDao;
import be.ugent.entitity.Patient;
 
@Path("/MachineLearningService")
public class MachineLearningService {
	@GET
	@Path("/tree")
	@Produces({ MediaType.APPLICATION_JSON })
	public Response getTree(@QueryParam("treeType") String tree, @HeaderParam("Authorization") String header) {
		if(!Authentication.isAuthorized(header)){
			return Response.status(403).build();
		}
		String retrieved = "";
		Runtime r = Runtime.getRuntime();
		Process p;
		BufferedReader b = null;
		try {
			p = r.exec("python /home/kdlannoy/HeadacheClassifier/heart_cart_dt_to_json.py");
			p.waitFor();
			b = new BufferedReader(new InputStreamReader(p.getInputStream()));
			String line = "";

			while ((line = b.readLine()) != null) {
			  retrieved += line;
			  System.out.println(line);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			try {
				b.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		Gson gson = new Gson();

		String s = "";
		try (BufferedReader in = new BufferedReader(new FileReader("/home/kdlannoy/HeadacheClassifier/cart_tree_heart.json")))
	    {
	        s= in.lines().collect(Collectors.joining("\n"));
	    } catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
//		System.out.println("Patient opgevraagd met naam: " + firstName + " " + lastName);
		
		
		return Response.ok(s+"").build();

	}
	
	
//	
//	@GET
//	@Path("/advice")
//	@Produces({ MediaType.TEXT_PLAIN })
//	public Response getAdvice(@QueryParam("patientID") String patientID, @HeaderParam("Authorization") String header) {
//		
//		if(!Authentication.isAuthorized(header)){
//			return Response.status(403).build();
//		}
////		System.out.println("Patient opgevraagd met naam: " + firstName + " " + lastName);
//		Patient retrieved = machineLearningDao.getPatienFromId(patientID);
//		
//		return Response.ok().entity(retrieved.getAdvice()+"").build();
//
//	}
//	
//	@GET
//	@Path("/login")
//	@Produces({ MediaType.APPLICATION_JSON })
//	public Response login(@HeaderParam("Authorization") String header) {
//		if(!Authentication.isAuthorized(header)){
//			return Response.status(403).build();
//		}		
////		System.out.println("User ingelogd met email:"+machineLearningDao.getPatientFromHeader(header));
//		Patient retrieved = machineLearningDao.getPatientFromHeader(header);
//		retrieved.setPassword("");
//		return Response.ok(retrieved+"").build();
//
//	}
//	
//	@POST
//	@Path("/patients/hello")
//	@Consumes({MediaType.TEXT_PLAIN})
//	public Response hello(String user){
//		
//		System.out.println("Hello "+user);
//		return Response.ok("Hello "+user).build();
//		
//	}
//
//	
//
//
//	@PUT
//	@Path("/patients")
//	@Consumes(MediaType.APPLICATION_JSON)
//	public Response addUser(String user){
//		
//		System.out.println("pat: "+user);
//		System.out.println("Patient requested to add: "+user.toString());
//		Gson gson = new Gson();
//		Patient toAdd = gson.fromJson(user, Patient.class);
//		if(toAdd.getPatientID()==0){
//			MachineLearningDao machineLearningDao = new MachineLearningDao();
//			toAdd.setPatientID(machineLearningDao.getNewId());
//		}
//		JSONObject userJSON = null;
//		try {
//			userJSON = new JSONObject(user);
//			toAdd.setRelation(""+userJSON.get("relation"));
//			
//		} catch (JSONException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//			return Response.status(417).build();
//		}
////		System.out.println("Patient to add:"+toAdd);
//		if(machineLearningDao.storePatient(toAdd)){
//			//return patient successfully created
//			return Response.status(201).entity(machineLearningDao.getPatienFromId(toAdd.getPatientID()+"")).build();
//		}else{
//			//return record was already in database, or was wrong format
//			return Response.status(409).build();
//		}
//	}
//
//	@POST
//	@Path("/patients/update")
//	@Consumes(MediaType.APPLICATION_JSON)
//	public Response changeUser(String user){
//		
//		System.out.println("Patient requested to change: "+user.toString());
//		Gson gson = new Gson();
//		Patient toAdd = gson.fromJson(user, Patient.class);
//		if(toAdd.getPatientID()<=0){
//			return Response.status(404).build();
//		}
//		
//		JSONObject userJSON = null;
//		try {
//			userJSON = new JSONObject(user);
//			toAdd.setRelation(""+userJSON.get("relation"));
//			
//			
//		} catch (JSONException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
////		System.out.println("Patient to add:"+toAdd);
//		if(machineLearningDao.updatePatient(toAdd)){
//			//return patient successfully created
//			return Response.status(202).entity(toAdd.getPatientID()).build();
//		}else{
//			//return record was already in database, or was wrong format
//			return Response.status(409).build();
//		}
//	}
//	
	
}
