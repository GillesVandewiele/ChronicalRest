package be.ugent;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;

@Path("/PatientService")
public class PatientService {
	PatientDao patientDao = new PatientDao();

	@GET
	@Path("/patients/")
	@Produces({ MediaType.APPLICATION_JSON })
	public Patient getUser(@QueryParam("firstName") String firstName, @QueryParam("lastName") String lastName, @Context HttpHeaders header, @Context HttpServletResponse response) {
		response.addHeader("Access-Control-Allow-Origin", "*");
		System.out.println("Patient opgevraagd met naam: "+firstName+" "+lastName);
		return patientDao.getPatient(firstName, lastName);
	
	}
}
