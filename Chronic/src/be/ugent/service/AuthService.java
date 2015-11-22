package be.ugent.service;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.bind.DatatypeConverter;

import com.owlike.genson.Genson;

import be.ugent.Authentication;
import be.ugent.dao.PatientDao;
import be.ugent.entitity.Patient;

@Path("/Auth")
public class AuthService {
		
	
	@GET
	@Path("/authTest")
	@Produces({ MediaType.APPLICATION_JSON })
	public Patient getUser(@Context HttpHeaders header, @Context HttpServletResponse response) {
		List<String> authList = header.getRequestHeader("Authorization");
		String auth = authList.get(0);
		
		
		System.out.println("Is authorized? "+Authentication.isAuthorized(auth));
		
		
		response.addHeader("Access-Control-Allow-Origin", "*");
		
		return new Patient();

	}



	
}
