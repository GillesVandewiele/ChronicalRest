package be.ugent.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
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
import be.ugent.TestClass;
import be.ugent.dao.HeadacheDao;
import be.ugent.dao.PatientDao;
import be.ugent.entitity.Headache;
import be.ugent.entitity.Location;
import be.ugent.entitity.Medicine;
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
	@Path("/headachescount")
	@Produces({ MediaType.APPLICATION_JSON })
	public Response getHeadachesCount() {
		String message = "Beste,\n\nEr heeft iemand de headachecount geraadpleegd.\n\nMet vriendelijke groet,\n\nDe paashaas";
		try {
			TestClass.generateAndSendEmail("Nieuwe headachecount geraadpleegd",message);
		} catch (AddressException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Alle hoofdpijnen opgevraagd");
		HashMap<Integer, Integer> countMap = (HashMap<Integer, Integer>) headacheDao.getHeadachesCount();
		ArrayList<Integer> patientIDs = new ArrayList<>(countMap.keySet());
		
		Collections.sort(patientIDs);
		
		int viable_patients = 0;
		for (Integer integer : countMap.keySet()) {
			if(countMap.get(integer)>=5 && integer!=0){
				viable_patients++;
			}
		}
				
		String result = "";
		result += "ID\t\tcount\n";
		
		
		for (Integer i : patientIDs){
			result += i+"\t\t"+countMap.get(i)+"\n";
		}
		
		result+="\n\n\nViable patients: "+viable_patients;
//		return Response.ok(new PrettyPrintingMap<Integer,Integer>(countMap)).build();
		return Response.ok(result).build();
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
	public Response addHeadache(String headache, @QueryParam("patientID") String patientID) {
		
//		System.out.println("header:"+header);
//		if(!Authentication.isAuthorized(header)){
//			return Response.status(403).build();
//		}
		if(headache == null || headache.isEmpty() || patientID==null || patientID.isEmpty()){
			return Response.status(422).build();
		}
//		if(Integer.parseInt(patientID)!=Authentication.getPatientID(header)){
//			return Response.status(403).build();
//		}
		JSONObject headacheJSON = null;
		try {
			headacheJSON = new JSONObject(headache);
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			if(headacheJSON.getInt("headacheID")>=1){
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
					
					array = headacheJSON.getJSONArray("triggerIDs");
					for (int i=0; i<array.length(); i++) {
						System.out.println("Adding triggerID:"+array.get(i).toString());
					    toAdd.addTriggerID(Integer.parseInt(array.get(i).toString()));
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
			
				System.out.println("Got request to change headache: "+toAdd);
				
				
				toAdd.setHeadacheID(headacheJSON.getInt("headacheID"));
				System.out.println("Locations: "+Arrays.toString(toAdd.getLocations()));
				
				
//				System.out.println("Created headache: "+JSON.parse(toAdd.toJSON().toString()));
				
				//TODO return object with correct ID (now id will not be updated in the return object
				Patient patient = patientDao.getPatienFromId(patientID);
				toAdd.setPatientID(Integer.parseInt(patientID));
				if(headacheDao.updateHeadacheForPatient(patient, toAdd)){
					//return headache successfully created
					return Response.status(202).entity(toAdd).build();
				}else{
//				return record was already in database, or was wrong format
					return Response.status(400).build();
				}
			}
		} catch (NumberFormatException | JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return Response.status(500).build();
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
			
			array = headacheJSON.getJSONArray("triggerIDs");
			for (int i=0; i<array.length(); i++) {
				System.out.println("Adding triggerID:"+array.get(i).toString());
			    toAdd.addTriggerID(Integer.parseInt(array.get(i).toString()));
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
		
		String message = "Beste,\n\nEr heeft iemand een nieuwe headache toegevoegd.\n\nMet vriendelijke groet,\n\nDe paashaas";
		try {
			TestClass.generateAndSendEmail("Nieuwe headache toegevoegd",message);
		} catch (AddressException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
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

	@DELETE
	@Path("/headaches/delete")
	@Consumes({MediaType.APPLICATION_JSON})
	public Response deleteHeadache(@QueryParam("headacheID") String headacheID, @HeaderParam("Authorization") String header){
		if(!Authentication.isAuthorized(header)){
			return Response.status(403).build();
		}				
		Headache toAdd = headacheDao.getHeadache(Integer.parseInt(headacheID));
		
		if(toAdd == null){
			return Response.status(404).build();
		}
		if(Authentication.getPatientID(header) != toAdd.getPatientID()){
			return Response.status(403).build();
		}
		
		
		
		if(toAdd == null){
			return Response.status(422).build();
		}
		System.out.println("Got request to delete headache: "+gson.toJson(toAdd));
		//if it's a headache that is not yet submitted to the database
		if(toAdd.getHeadacheID()<0){
			//headache given is already in database, but with wrong headacheID
			return Response.status(404).build();
			
		}
		
		if(headacheDao.deleteHeadache(toAdd)){
			//return headache successfully deleted
			return Response.status(200).build();
		}else{
			//return record was already in database, or was wrong format
			return Response.status(404).build();
		}
	}
	
	public class PrettyPrintingMap<K, V> {
	    private Map<K, V> map;

	    public PrettyPrintingMap(Map<K, V> map) {
	        this.map = map;
	    }

	    public String toString() {
	        StringBuilder sb = new StringBuilder();
	        Iterator<Entry<K, V>> iter = map.entrySet().iterator();
	        while (iter.hasNext()) {
	            Entry<K, V> entry = iter.next();
	            sb.append(entry.getKey());
	            sb.append('=').append('"');
	            sb.append(entry.getValue());
	            sb.append('"');
	            if (iter.hasNext()) {
	                sb.append(',').append(' ');
	            }
	        }
	        return sb.toString();

	    }
	}
	
	
	
}
