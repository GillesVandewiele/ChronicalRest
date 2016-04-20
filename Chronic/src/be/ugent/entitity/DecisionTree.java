package be.ugent.entitity;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "patient")
public class DecisionTree implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String type; //Type is: CART, QUEST, C4.5, OWN, MERGE
	private int dokterID; // dokterID is patient with isDocter==1
	private String timestamp; // timestamp of injection in db (as string repr)
	private String JSON_repr; // 
		
	public DecisionTree() {
		// TODO Auto-generated constructor stub
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getDokterID() {
		return dokterID;
	}

	public void setDokterID(int dokterID) {
		this.dokterID = dokterID;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public String getJSON_repr() {
		return JSON_repr;
	}

	public void setJSON_repr(String jSON_repr) {
		JSON_repr = jSON_repr;
	}	
}
