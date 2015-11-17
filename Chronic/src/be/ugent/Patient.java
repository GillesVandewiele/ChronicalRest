package be.ugent;

import java.io.Serializable;
import java.util.Date;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "patient")
public class Patient implements Serializable {
	private static final long serialVersionUID = 1L;
	private int id;
	private String firstName;
	private String lastName;
	private boolean isMale;
	private Date birthDate;
	private boolean isEmployed;
	private enum relation{
		GETROUWD, IN_RELATIE, VRIJGEZEL
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


	public Date getBirthDate() {
		return birthDate;
	}

	@XmlElement
	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}


	public boolean isEmployed() {
		return isEmployed;
	}

	@XmlElement
	public void setEmployed(boolean isEmployed) {
		this.isEmployed = isEmployed;
	}


	public int getId() {
		return id;
	}

	@XmlElement
	public void setId(int id){
		this.id = id;
	}
	
}
