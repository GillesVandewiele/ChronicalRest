package be.ugent.entitity;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "patient")
public class Diagnose implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private int diagnoseID;
	private String name;
	private String description;
		
	public Diagnose() {
		// TODO Auto-generated constructor stub
	}

	public int getDiagnoseID() {
		return diagnoseID;
	}

	public void setDiagnoseID(int diagnoseID) {
		this.diagnoseID = diagnoseID;
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
