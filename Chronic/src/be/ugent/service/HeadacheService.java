package be.ugent.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;

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
import be.ugent.entitity.Location;
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
		System.out.println("Alle hoofdpijnen opgevraagd van patient met id:"+patientID);
		return Response.ok(headacheDao.getAllHeadachesForPatient(Integer.parseInt(patientID))).build();
	}
	
	@GET
	@Path("/headaches/semantics")
	@Produces({ MediaType.TEXT_PLAIN })
	public Response getHeadacheSemantics(@HeaderParam("Authorization") String header, @QueryParam("patientID") String patientID) {
		if(patientID == null)
			return Response.ok(headacheDao.getSemantics("<http://tw06v033.ugent.be/Chronic/rest/HeadacheService/headaches>")).build();
		
		if(patientDao.getPatienFromId(patientID) != null)
			return Response.ok(headacheDao.getSemantics("<http://tw06v033.ugent.be/Chronic/rest/HeadacheService/headaches?patientID="+patientID+">")).build();
		else
			return Response.status(404).build();
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
		if(headache == null || patientID==null || patientID.isEmpty()){
			return Response.status(422).build();
		}
		if(Integer.parseInt(patientID)!=Authentication.getPatientID(header)){
			return Response.status(403).build();
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
			JSONArray array = headacheJSON.getJSONArray("intensityValues");
			for (int i=0; i<array.length(); i++) {
			    JSONObject item = array.getJSONObject(i);
			    System.out.println("Item in intensityValues:"+item.toString());
			    toAdd.addIntensityValue(item.getString("key"), item.getString("value"));
			}
			
			toAdd.setEnd(headacheJSON.getString("end"));
			
			array = headacheJSON.getJSONArray("symptomIDs");
			for (int i=0; i<array.length(); i++) {
				System.out.println("Adding symptomID:"+array.get(i).toString());
			    toAdd.addSymptomID(Integer.parseInt(array.get(i).toString()));
			}
			
			JSONObject locations = headacheJSON.getJSONObject("locations");
			Iterator it = locations.keys();
			while(it.hasNext()){
				String loc = ""+it.next();
				toAdd.addLocation(new Location(loc.toString(), (boolean)locations.get(loc)));
				System.out.println("Location: "+loc.toString()+":"+locations.get(""+loc));
				it.remove();
			}
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		System.out.println("Got request to add headache: "+toAdd);
		
		
		toAdd.setHeadacheID(headacheDao.getNewHeadacheID());
		System.out.println("Locations: "+Arrays.toString(toAdd.getLocations()));
		
		
//		System.out.println("Created headache: "+JSON.parse(toAdd.toJSON().toString()));
		
		//TODO return object with correct ID (now id will not be updated in the return object
		Patient patient = patientDao.getPatienFromId(patientID);
		toAdd.setPatientID(Integer.parseInt(patientID));
		if(headacheDao.addHeadacheForPatient(patient, toAdd)){
			//return headache successfully created
			return Response.status(201).entity(toAdd).build();
		}else{
//			return record was already in database, or was wrong format
			return Response.status(409).build();
		}
	}

	
	
	
	
}
