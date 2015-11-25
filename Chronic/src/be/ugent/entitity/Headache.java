package be.ugent.entitity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "patient")
public class Headache implements Serializable {
	private static final long serialVersionUID = 1L;
	//primary key
	private int headacheID;
	private Map<Date, Integer> intensityValues;
	
	//foreign keys
	private int patientID;
	
	private Date end;
	
	private Set<Location> locations;
	private Set<Integer> symptomIDs;
	private Set<Integer> triggerIDs;
	
		
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


	public Map<Date, Integer> getIntensityValues() {
		return intensityValues;
	}


	public void setIntensityValues(Map<Date, Integer> intensityValues) {
		this.intensityValues = intensityValues;
	}

	public void addIntensityValue(Date key, int value){
		intensityValues.put(key, value);
	}
	
	public boolean removeIntensityValue(Date date){
		for (Date key : intensityValues.keySet()) {
			if(key.getTime() == date.getTime()){
				intensityValues.remove(key);
				return true;
			}
		}
		return false;
		
	}

	public Date getEnd() {
		return end;
	}


	public void setEnd(Date end) {
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

}

