package be.ugent.entitity;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "patient")
public class Medicine implements Serializable {
	private static final long serialVersionUID = 1L;
	//primary key
	private int medicineID;
	
	//foreign keys
	private int patientID;
	private int drugID;
	
	private String Date;
	private float quantity;
	
		
	public Medicine() {
		// TODO Auto-generated constructor stub
	}


	public int getMedicineID() {
		return medicineID;
	}


	public void setMedicineID(int medicineID) {
		this.medicineID = medicineID;
	}


	public int getPatientID() {
		return patientID;
	}


	public void setPatientID(int patientID) {
		this.patientID = patientID;
	}


	public int getDrugID() {
		return drugID;
	}


	public void setDrugID(int drugID) {
		this.drugID = drugID;
	}


	public String getDate() {
		return Date;
	}


	public void setDate(String date) {
		Date = date;
	}


	public float getQuantity() {
		return quantity;
	}


	public void setQuantity(float quantity) {
		this.quantity = quantity;
	}

	
	
	
}
