package be.ugent.entitity;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "patient")
public class DailyMedicine implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private int dailyMedicineID;
	private int medicineID;
	
	public DailyMedicine() {
		// TODO Auto-generated constructor stub
	}

	public int getDailyMedicineID() {
		return dailyMedicineID;
	}

	public void setDailyMedicineID(int dailyMedicineID) {
		this.dailyMedicineID = dailyMedicineID;
	}

	public int getMedicineID() {
		return medicineID;
	}

	public void setMedicineID(int medicineID) {
		this.medicineID = medicineID;
	}

	
}
