package be.ugent.entitity;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.google.gson.Gson;

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
	private String password;

	public String relation;
	
	private String advice;
	private int diagnoseID;
	
	private int isDocter;

	public String getRelation() {
		return relation;
	}


	public void setRelation(String relation) {
		this.relation = relation;
	}


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


	public int getDiagnoseID() {
		return diagnoseID;
	}


	public void setDiagnoseID(int diagnoseID) {
		this.diagnoseID = diagnoseID;
	}


	public String getEmail() {
		return email;
	}


	public void setEmail(String email) {
		this.email = email;
	}


	public String getPassword() {
		return password;
	}


	public void setPassword(String password) {
		this.password = password;
	}
	
	@Override
	public String toString(){
		Gson gson = new Gson();
		String returnString = "{";
		returnString += "\"patientID\":"+patientID+",\n"+
		"\"firstName\":\""+firstName+"\",\n"+
				"\"lastName\":\""+lastName+"\",\n"+
		"\"email\":\""+email+"\",\n"+
				"\"isMale\":"+isMale+",\n"+
		"\"birthDate\":\""+birthDate+"\",\n"+
				"\"isEmployed\":"+isEmployed+",\n"+
		"\"password\":\""+password+"\",\n"+
				"\"relation\":\""+relation+"\",\n"+
		"\"advice\":\""+advice+"\",\n"+
				"\"diagnoseID\":\""+diagnoseID+"\"\n"+
		"}";
		return returnString;
	}


	public String getFullName() {
		// TODO Auto-generated method stub
		return getFirstName()+" "+getLastName();
	}


	public int getIsDocter() {
		return isDocter;
	}


	public void setIsDocter(int isDocter) {
		this.isDocter = isDocter;
	}
	
}
