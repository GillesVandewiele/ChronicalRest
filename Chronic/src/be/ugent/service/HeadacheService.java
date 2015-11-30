package be.ugent.service;

import java.util.ArrayList;
import java.util.HashMap;

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

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.github.jsonldjava.utils.JsonUtils;
import com.google.gson.Gson;
import com.mongodb.util.JSON;

import be.ugent.Authentication;
import be.ugent.dao.HeadacheDao;
import be.ugent.dao.PatientDao;
import be.ugent.entitity.Headache;
import be.ugent.entitity.Pair;
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
	
	
	
	@Path("/headaches/ld")
	@Produces({ MediaType.APPLICATION_JSON })
	@Consumes({MediaType.APPLICATION_JSON})
	public Response getFirstHeadache( @QueryParam("patientID") String patientID) {
		HeadacheDao headacheDao = new HeadacheDao();
		Object jaja = headacheDao.getAllHeadachesForPatient(Integer.parseInt(patientID)).get(0).toJsonLD();
		return Response.ok(jaja).build();
		
	}
	

	@POST
	@Path("/headaches")
	@Consumes({MediaType.APPLICATION_JSON})
	public Response addHeadache(String headache, @HeaderParam("Authorization") String header, @QueryParam("patientID") String patientID) {
		
//		System.out.println("header:"+header);
		if(!Authentication.isAuthorized(header)){
			return Response.status(403).build();
		}
		if(headache == null){
			return Response.status(422).build();
		}
		JSONObject headacheJSON = null;
		try {
			headacheJSON = new JSONObject(headache);
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		System.out.println("object:"+headacheJSON);
		Headache toAdd = new Headache();
		try {
			toAdd.setEnd(""+headacheJSON.get("end"));
//			toAdd.setLocations((JSONArray)headacheJSON.getJSONArray("location"));
			toAdd.setSymptomIDs((JSONArray)headacheJSON.getJSONArray("symptoms"));
			toAdd.setTriggerIDs((JSONArray)headacheJSON.getJSONArray("triggers"));
			ArrayList<Pair> values = new ArrayList<>();
			JSONArray array = headacheJSON.getJSONArray("intensityValues");
	        for(int i = 0; i < array.length(); i++){
	            String key = array.getJSONObject(i).getString("key");
	            String value = array.getJSONObject(i).getString("value");
	            values.add(new Pair(key, value));
	        }
			
			
			
			toAdd.setIntensityValues(values);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		System.out.println("JSON?"+headache);
		System.out.println("Got request to add headache: "+toAdd);
		
		
		toAdd.setHeadacheID(headacheDao.getNewHeadacheID());
		
		
		System.out.println("Created headache: "+JSON.parse(toAdd.toJSON().toString()));
		
		//TODO return object with correct ID (now id will not be updated in the return object
		Patient patient = patientDao.getPatienFromId(patientID);
		if(headacheDao.addHeadacheForPatient(patient, toAdd)){
			//return headache successfully created
			return Response.status(201).entity(toAdd).build();
		}else{
//			return record was already in database, or was wrong format
			return Response.status(409).build();
		}
	}

	
	
	
	
}
