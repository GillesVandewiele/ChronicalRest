package be.ugent.entitity;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "patient")
public class Drug implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private int drugID;
	private String name;
	private String description;
		
	public Drug() {
		// TODO Auto-generated constructor stub
	}

	public int getDrugID() {
		return drugID;
	}

	public void setDrugID(int drugID) {
		this.drugID = drugID;
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
