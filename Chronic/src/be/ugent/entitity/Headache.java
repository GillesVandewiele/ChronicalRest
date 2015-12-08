package be.ugent.entitity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import javax.ws.rs.core.Response;
import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.github.jsonldjava.core.JsonLdOptions;
import com.github.jsonldjava.core.JsonLdProcessor;
import com.github.jsonldjava.utils.JsonUtils;
import com.google.gson.Gson;
import com.mongodb.util.JSON;

import be.ugent.dao.PatientDao;

@XmlRootElement(name = "patient")
public class Headache implements Serializable {
	private static final long serialVersionUID = 1L;
	//primary key
	private int headacheID;
	private Pair[] intensityValues = new Pair[0];
	
	//foreign keys
	private int patientID;
	
	private String end;
	
	private Location[] locations = new Location[0];
	private int[] symptomIDs = new int[0];
	private int[] triggerIDs = new int[0];
	
		
	public Headache() {
		// TODO Auto-generated constructor stub
	}


	public int getHeadacheID() {
		return headacheID;
	}


	public void setHeadacheID(int headacheID) {
		this.headacheID = headacheID;
	}


	public int getPatientID() {
		return patientID;
	}


	public void setPatientID(int patientID) {
		this.patientID = patientID;
	}


	public Pair[] getIntensityValues() {
		return intensityValues;
	}


	public void setIntensityValues(Pair[] intensityValues) {
		this.intensityValues = intensityValues;
	}

	public void addIntensityValue(String key, String value){
		Pair pair = new Pair(key, ""+value);
		Pair[] tempValues = new Pair[intensityValues.length+1];
		for (int i = 0; i < intensityValues.length; i++) {
			tempValues[i] = intensityValues[i];
		}
		tempValues[intensityValues.length] = pair;
		this.intensityValues = tempValues;
	}
	
//	public boolean removeIntensityValue(Date date){
//		for (Pair key : intensityValues) {
//			if(key.getKey().equals(date+"")){
//				intensityValues.remove(key);
//				return true;
//			}
//		}
//		return false;
//		
//	}

	public String getEnd() {
		return end;
	}


	public void setEnd(String end) {
		this.end = end;
	}


	public Location[] getLocations() {
		return locations;
	}


	public void setLocations(Location[] location) {
		this.locations = location;
	}
	
	public void addLocation(Location loc){
		int length = this.locations.length;
		Location[] tempLocations = new Location[length+1];
	    System.arraycopy(this.locations, 0, tempLocations, 0, this.locations.length);
	    tempLocations[length] = loc;
	    this.locations = tempLocations;
	}
	
//	public boolean removeLocation(Location loc){
//		for (Location location : locations) {
//			if(location.equals(loc)){
//				locations.remove(location);
//				return true;
//			}
//		}
//		return false;
//	}

	public int[] getSymptomIDs() {
		return symptomIDs;
	}


	public void setSymptomIDs(int[] symptoms) {
		this.symptomIDs = symptoms;
	}

	public void addSymptomID(Symptom symptom){
		int length = this.symptomIDs.length;
		int[] tempSymptoms = new int[length+1];
	    System.arraycopy(this.symptomIDs, 0, tempSymptoms, 0, this.symptomIDs.length);
	    tempSymptoms[length] = symptom.getSymptomID();
	    this.symptomIDs = tempSymptoms;
	}
	
	public void addSymptomID(int id){
		int length = this.symptomIDs.length;
		int[] tempSymptoms = new int[length+1];
	    System.arraycopy(this.symptomIDs, 0, tempSymptoms, 0, this.symptomIDs.length);
	    tempSymptoms[length] = id;
	    this.symptomIDs = tempSymptoms;
	}
	
//	public boolean removeSymptomID(Symptom symptom){
//		return symptomIDs.remove(symptom.getSymptomID());
//	}
//	
//	public boolean removeSymptomID(int id){
//		return symptomIDs.remove(id);
//		
//	}

	public int[] getTriggerIDs() {
		return triggerIDs;
	}


	public void setTriggerIDs(int[] triggers) {
		this.triggerIDs = triggers;
	}
	
	public void addTriggerID(Trigger trigger){
		int length = this.triggerIDs.length;
		int[] tempTriggers = new int[length+1];
	    System.arraycopy(this.triggerIDs, 0, tempTriggers, 0, this.triggerIDs.length);
	    tempTriggers[length] = trigger.getTriggerID();
	    this.triggerIDs = tempTriggers;
	}
	
	public void addTriggerID(int id){
		int length = this.triggerIDs.length;
		int[] tempTriggers = new int[length+1];
	    System.arraycopy(this.triggerIDs, 0, tempTriggers, 0, this.triggerIDs.length);
	    tempTriggers[length] = id;
	    this.triggerIDs = tempTriggers;
	}
	
//	public boolean removeTriggerID(Trigger trigger){
//		return triggerIDs.remove(trigger.getTriggerID());
//	}
//	
//	public boolean removeTriggerID(int id){
//		return triggerIDs.remove(id);
//	}

	public Object toJsonLD(){
		try {
			// Read the file into an Object (The type of this object will be a List, Map, String, Boolean,
			// Number or null depending on the root object in the file).
			
			Gson gson = new Gson();
			Object jsonObject = gson.toJson(this);
			// Create a context JSON map containing prefixes and definitions
			String context = "\"@context\": {"+
			"\"description\": \"http://example.org/description\","+
			"\"discrete\": \"http://example.org/discrete\","+
			"\"location\": \"http://example.org/location\","+
			"\"semantics\": \"http://example.org/semantics\","+
			"\"unit\": \"http://example.org/unit\","+
			"\"value\": \"http://example.org/value\""+
			"}";
			
			PatientDao patientDao = new PatientDao();
			Random rand = new Random();
//			\"description\": \"Test\"
			String values = "\"id\":"+"\""+getPatientID()+"\","+
							"\"description\":"+"\"Hoofdpijn Intensiteiten\" ,"+
							"\"discrete\": \"true\","+
							"\"location\": \""+ patientDao.getPatienFromId(this.getPatientID()+"").getFullName()+"\","+
							"\"semantics\": \"null\","+
							"\"unit\": \"\","+
							"\"value\": \""+(5+rand.nextInt(1))+""+"\",";
							
					
			
			
			;
			String output = "{"+context+","+values +"}";
			
			Object compact =JsonLdProcessor.expand(JSON.parse(output)); 
//					JsonLdProcessor.expand(JSON.parse(graph), context, options);
			System.out.println(compact+"");
			// Print out the result (or don't, it's your call!)
//			System.out.println(JsonUtils.toPrettyString(compact));
			return JsonUtils.toPrettyString(compact);
		}catch(Exception e){
			System.err.println("ER ZIT EEN FOUT IN DE HEADACHE TO LD SHIT");
		}
		return "";
	}
	
	public JSONObject toJSON(){
		Gson gson = new Gson();
		JSONObject result = new JSONObject();
		try {
			result.append("headacheID", headacheID);
			JSONArray intensVals = new JSONArray();
			for (Pair pair : intensityValues) {
				System.out.println("got Pair:"+pair);
				intensVals.put(gson.toJson(pair));
			}
			result.append("intensityValues", intensVals);
			result.append("patientID", patientID);
			result.append("end", end);
			JSONArray locs = new JSONArray();
			for (Location location : locations) {
				locs.put(location);
			}
			
			result.append("locations", locs);
			
			JSONArray sympts = new JSONArray();
			for (Integer integer : symptomIDs) {
				sympts.put(integer);
			}
			
			result.append("symptomIDs", sympts);
			
			JSONArray triggs = new JSONArray();
			for (Integer integer : triggerIDs) {
				sympts.put(integer);
			}
			result.append("triggerIDs", triggs);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		return result;
	}


//	public void setLocations(JSONArray jsonObject) {
//		for(int i = 0; i < jsonObject.length(); i++){
//            try {
//				String key = jsonObject.getJSONObject(i).getString("location");
//				String value = jsonObject.getJSONObject(i).getString("value");
//	            locations.add(new Location());
//			} catch (JSONException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//            
//        }
//		
//	}


//	public void setSymptomIDs(JSONArray jsonArray) {
//		for(int i = 0; i < jsonArray.length(); i++){
//            try {
//            	if(jsonArray.getJSONObject(i).getBoolean("val")){
//            		symptomIDs.add(jsonArray.getJSONObject(i).getInt("id"));
//            	}
//			} catch (JSONException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//            
//        }
//		
//	}
	
//	public void setTriggerIDs(JSONArray jsonArray) {
//		for(int i = 0; i < jsonArray.length(); i++){
//            try {
//            	if(jsonArray.getJSONObject(i).getBoolean("val")){
//            		triggerIDs.add(jsonArray.getJSONObject(i).getInt("id"));
//            	}
//			} catch (JSONException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//            
//        }
//		
//	}
}

