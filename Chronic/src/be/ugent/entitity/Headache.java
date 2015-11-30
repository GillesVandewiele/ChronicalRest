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
	private ArrayList<Pair> intensityValues;
	
	//foreign keys
	private int patientID;
	
	private String end;
	
	private Set<Location> locations = new HashSet<Location>();
	private Set<Integer> symptomIDs = new HashSet<Integer>();
	private Set<Integer> triggerIDs = new HashSet<Integer>();
	
		
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


	public ArrayList<Pair> getIntensityValues() {
		return intensityValues;
	}


	public void setIntensityValues(ArrayList<Pair> intensityValues) {
		this.intensityValues = intensityValues;
	}

	public void addIntensityValue(Date key, int value){
		Pair pair = new Pair(key.toString(), ""+value);
		intensityValues.add(pair);
	}
	
	public boolean removeIntensityValue(Date date){
		for (Pair key : intensityValues) {
			if(key.getKey().equals(date+"")){
				intensityValues.remove(key);
				return true;
			}
		}
		return false;
		
	}

	public String getEnd() {
		return end;
	}


	public void setEnd(String end) {
		this.end = end;
	}


	public Set<Location> getLocations() {
		return locations;
	}


	public void setLocations(Set<Location> location) {
		this.locations = location;
	}
	
	public void addLocation(Location loc){
		locations.add(loc);
	}
	
	public boolean removeLocation(Location loc){
		for (Location location : locations) {
			if(location.equals(loc)){
				locations.remove(location);
				return true;
			}
		}
		return false;
	}

	public Set<Integer> getSymptomIDs() {
		return symptomIDs;
	}


	public void setSymptomIDs(Set<Integer> symptoms) {
		this.symptomIDs = symptoms;
	}

	public void addSymptomID(Symptom symptom){
		symptomIDs.add(symptom.getSymptomID());
	}
	
	public void addSymptomID(int id){
		symptomIDs.add(id);
	}
	
	public boolean removeSymptomID(Symptom symptom){
		return symptomIDs.remove(symptom.getSymptomID());
	}
	
	public boolean removeSymptomID(int id){
		return symptomIDs.remove(id);
		
	}

	public Set<Integer> getTriggerIDs() {
		return triggerIDs;
	}


	public void setTriggerIDs(Set<Integer> triggers) {
		this.triggerIDs = triggers;
	}
	
	public void addTriggerID(Trigger trigger){
		triggerIDs.add(trigger.getTriggerID());
	}
	
	public void addTriggerID(int id){
		triggerIDs.add(id);
	}
	
	public boolean removeTriggerID(Trigger trigger){
		return triggerIDs.remove(trigger.getTriggerID());
	}
	
	public boolean removeTriggerID(int id){
		return triggerIDs.remove(id);
	}

	public Object toJsonLD(){
		try {
			// Read the file into an Object (The type of this object will be a List, Map, String, Boolean,
			// Number or null depending on the root object in the file).
			
			Gson gson = new Gson();
			Object jsonObject = gson.toJson(this);
			// Create a context JSON map containing prefixes and definitions
			String context = "\"@context\": {"+
			"\"description\": \"http://example.org/description\""+
			"\"discrete\": \"http://example.org/discrete\""+
			"\"location\": \"http://example.org/location\""+
			"\"semantics\": \"http://example.org/semantics\""+
			"\"unit\": \"http://example.org/unit\""+
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


	public void setLocations(JSONArray jsonObject) {
		for(int i = 0; i < jsonObject.length(); i++){
            try {
				String key = jsonObject.getJSONObject(i).getString("location");
				String value = jsonObject.getJSONObject(i).getString("value");
	            locations.add(new Location());
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            
        }
		
	}


	public void setSymptomIDs(JSONArray jsonArray) {
		for(int i = 0; i < jsonArray.length(); i++){
            try {
            	if(jsonArray.getJSONObject(i).getBoolean("val")){
            		symptomIDs.add(jsonArray.getJSONObject(i).getInt("id"));
            	}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            
        }
		
	}
	
	public void setTriggerIDs(JSONArray jsonArray) {
		for(int i = 0; i < jsonArray.length(); i++){
            try {
            	if(jsonArray.getJSONObject(i).getBoolean("val")){
            		triggerIDs.add(jsonArray.getJSONObject(i).getInt("id"));
            	}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            
        }
		
	}
}

