package be.ugent.service;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.google.gson.Gson;

import be.ugent.Authentication;
import be.ugent.dao.DiagnoseDao;
import be.ugent.entitity.Diagnose;

@Path("/DiagnoseService")
public class DiagnoseService {
	DiagnoseDao diagnoseDao = new DiagnoseDao();
	Gson gson = new Gson();
	
	@GET
	@Path("/diagnoses")
	@Produces({ MediaType.APPLICATION_JSON })
	public Response getAllDiagnoses(@HeaderParam("Authorization") String header) {
		System.out.println("header:" + header);
		if (!Authentication.isAuthorized(header)) {
			return Response.status(403).build();
		}
		return Response.ok(diagnoseDao.getAllDiagnoses()).build();
	}

	@PUT
	@Path("/diagnoses")
	@Consumes({ MediaType.APPLICATION_JSON })
	public Response addDiagnose(Diagnose diagnose, @HeaderParam("Authorization") String header) {
		System.out.println("header:" + header);
		if (!Authentication.isAuthorized(header)) {
			return Response.status(403).build();
		}
		Diagnose toAdd = diagnose;
		
		if (toAdd == null) {
			return Response.status(422).build();
		}

		System.out.println("Got request to add diagnose: " + gson.toJson(diagnose));

		toAdd.setDiagnoseID(diagnoseDao.getNewDiagnoseID());

		System.out.println("Created diagnose: " + gson.toJson(toAdd));

		if (diagnoseDao.addDiagnose(toAdd)) {
			// return diagnose successfully created
			return Response.status(201).entity(diagnoseDao.getDiagnose(toAdd.getName())).build();
		} else {
			// return record was already in database, or was wrong format
			return Response.status(409).build();
		}
	}

	@POST
	@Path("/diagnoses/update")
	@Consumes({ MediaType.APPLICATION_JSON })
	public Response changeDiagnose(Diagnose diagnose, @HeaderParam("Authorization") String header) {
		if (!Authentication.isAuthorized(header)) {
			return Response.status(403).build();
		}

		Diagnose toAdd = diagnose;

		if (toAdd == null) {
			return Response.status(422).build();
		}
		System.out.println("Got request to update diagnose: " + gson.toJson(diagnose));
		// if it's a diagnose that is not yet submitted to the database
		if (diagnose.getDiagnoseID() == -1) {
			int id = diagnoseDao.getNewDiagnoseID();
			diagnose.setDiagnoseID(id);
			if (diagnoseDao.addDiagnose(diagnose)) {
				return Response.status(201).entity(diagnoseDao.getDiagnose(toAdd.getName())).build();
			} else {
				// diagnose given is already in database, but with wrong diagnoseID
				return Response.status(409).build();
			}
		}

		System.out.println("Created diagnose: " + gson.toJson(toAdd));

		if (diagnoseDao.changeDiagnose(toAdd)) {
			// return diagnose successfully created
			return Response.status(202).entity(diagnoseDao.getDiagnose(toAdd.getName())).build();
		} else {
			// return record was already in database, or was wrong format
			return Response.status(409).build();
		}
	}

	@DELETE
	@Path("/diagnoses/delete")
	@Consumes({ MediaType.APPLICATION_JSON })
	public Response deleteDiagnose(Diagnose diagnose, @HeaderParam("Authorization") String header) {
		if (!Authentication.isAuthorized(header)) {
			return Response.status(403).build();
		}

		Diagnose toAdd = diagnose;

		if (toAdd == null) {
			return Response.status(422).build();
		}
		System.out.println("Got request to delete diagnose: " + gson.toJson(diagnose));
		// if it's a diagnose that is not yet submitted to the database
		if (diagnose.getDiagnoseID() < 0) {
			// diagnose given is already in database, but with wrong diagnoseID
			return Response.status(404).build();

		}

		if (diagnoseDao.deleteDiagnose(toAdd)) {
			// return diagnose successfully deleted
			return Response.status(200).build();
		} else {
			// return record was already in database, or was wrong format
			return Response.status(404).build();
		}
	}

}
