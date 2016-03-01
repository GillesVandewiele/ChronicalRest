package be.ugent.entitity;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "version")
public class Version implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String versionID;
	private String description;
		
	public Version() {
		// TODO Auto-generated constructor stub
	}

	public String getVersionID() {
		return versionID;
	}

	public void setVersionID(String versionID) {
		this.versionID = versionID;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	
	
	
	
}
