package be.ugent.entitity;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "trigger")
public class Trigger implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private int triggerID;
	private String name;
	private String description;
		
	public Trigger() {
		// TODO Auto-generated constructor stub
	}

	public int getTriggerID() {
		return triggerID;
	}

	public void setTriggerID(int drugID) {
		this.triggerID = drugID;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	
	
}
