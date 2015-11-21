package be.ugent.entitity;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "patient")
public class Patient implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private int patientID;
	private String firstName;
	private String lastName;
	private String email;
	private boolean isMale;
	private String birthDate;
	private boolean isEmployed;
	public enum relation{
		GETROUWD, IN_RELATIE, VRIJGEZEL
	}
	private String advice;
	private String diagnosis;
	
	
	public Patient() {
		// TODO Auto-generated constructor stub
	}
	
	
	public String getFirstName() {
		return firstName;
	}

	@XmlElement
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}


	public String getLastName() {
		return lastName;
	}

	@XmlElement
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}


	public boolean isMale() {
		return isMale;
	}

	@XmlElement
	public void setMale(boolean isMale) {
		this.isMale = isMale;
	}


	public String getBirthDate() {
		return birthDate;
	}

	@XmlElement
	public void setBirthDate(String birthDate) {
		this.birthDate = birthDate;
	}


	public boolean isEmployed() {
		return isEmployed;
	}

	@XmlElement
	public void setEmployed(boolean isEmployed) {
		this.isEmployed = isEmployed;
	}

	public int getPatientID() {
		return patientID;
	}


	public void setPatientID(int patientID) {
		this.patientID = patientID;
	}


	public String getAdvice() {
		return advice;
	}


	public void setAdvice(String advice) {
		this.advice = advice;
	}


	public String getDiagnosis() {
		return diagnosis;
	}


	public void setDiagnosis(String diagnosis) {
		this.diagnosis = diagnosis;
	}


	public String getEmail() {
		return email;
	}


	public void setEmail(String email) {
		this.email = email;
	}
	
	
	
}
