 	package be.ugent.entitity;

import java.io.Serializable;
import java.util.Date;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "patient")
public class Medicine implements Serializable {
	private static final long serialVersionUID = 1L;
	//primary key
	private int medicineID;
	
	//foreign keys
	private int patientID;
	private int drugID;
	
	private String date;
	private double quantity;
	
		
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
		return date;
	}


	public void setDate(String date) {
		this.date = date;
	}


	public double getQuantity() {
		return quantity;
	}


	public void setQuantity(double quantity) {
		this.quantity = quantity;
	}

	
	
	
}
