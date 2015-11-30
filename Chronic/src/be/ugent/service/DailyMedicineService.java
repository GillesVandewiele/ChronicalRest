package be.ugent.service;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

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

import com.github.jsonldjava.core.JsonLdError;
import com.github.jsonldjava.core.JsonLdOptions;
import com.github.jsonldjava.core.JsonLdProcessor;
import com.github.jsonldjava.utils.JsonUtils;
import com.google.gson.Gson;
import com.mongodb.util.JSON;

import be.ugent.Authentication;
import be.ugent.dao.DailyMedicineDao;
import be.ugent.entitity.DailyMedicine;

@Path("/DailyMedicineService")
public class DailyMedicineService {
	DailyMedicineDao dailyMedicineDao = new DailyMedicineDao();
	Gson gson = new Gson();
	
	

	@GET
	@Path("/dailyMedicines")
	@Produces({ MediaType.APPLICATION_JSON })
	public Response getAllDailyMedicines() {
		return Response.ok(dailyMedicineDao.getAllDailyMedicines()).build();
	}

	@PUT
	@Path("/dailyMedicines")
	@Consumes({ MediaType.APPLICATION_JSON })
	public Response addDailyMedicine(DailyMedicine dailyMedicine, @HeaderParam("Authorization") String header) {
		System.out.println("header:" + header);
		if (!Authentication.isAuthorized(header)) {
			return Response.status(403).build();
		}
		DailyMedicine toAdd = dailyMedicine;
		
		if (toAdd == null) {
			return Response.status(422).build();
		}

		System.out.println("Got request to add dailyMedicine: " + gson.toJson(dailyMedicine));

		toAdd.setDailyMedicineID(dailyMedicineDao.getNewDailyMedicineID());

		System.out.println("Created dailyMedicine: " + gson.toJson(toAdd));

		if (dailyMedicineDao.addDailyMedicine(toAdd)) {
			// return dailyMedicine successfully created
			return Response.status(201).entity(dailyMedicineDao.getDailyMedicine(toAdd.getMedicineID())).build();
		} else {
			// return record was already in database, or was wrong format
			return Response.status(409).build();
		}
	}

	@POST
	@Path("/dailyMedicines/update")
	@Consumes({ MediaType.APPLICATION_JSON })
	public Response changeDailyMedicine(DailyMedicine dailyMedicine, @HeaderParam("Authorization") String header) {
		if (!Authentication.isAuthorized(header)) {
			return Response.status(403).build();
		}

		DailyMedicine toAdd = dailyMedicine;

		if (toAdd == null) {
			return Response.status(422).build();
		}
		System.out.println("Got request to update dailyMedicine: " + gson.toJson(dailyMedicine));
		// if it's a dailyMedicine that is not yet submitted to the database
		if (dailyMedicine.getDailyMedicineID() == -1) {
			int id = dailyMedicineDao.getNewDailyMedicineID();
			dailyMedicine.setDailyMedicineID(id);
			if (dailyMedicineDao.addDailyMedicine(dailyMedicine)) {
				return Response.status(201).entity(dailyMedicineDao.getDailyMedicine(toAdd.getDailyMedicineID())).build();
			} else {
				// dailyMedicine given is already in database, but with wrong dailyMedicineID
				return Response.status(409).build();
			}
		}

		System.out.println("Created dailyMedicine: " + gson.toJson(toAdd));

		if (dailyMedicineDao.changeDailyMedicine(toAdd)) {
			// return dailyMedicine successfully created
			return Response.status(202).entity(dailyMedicineDao.getDailyMedicine(toAdd.getDailyMedicineID())).build();
		} else {
			// return record was already in database, or was wrong format
			return Response.status(409).build();
		}
	}

	@DELETE
	@Path("/dailyMedicines/delete")
	@Consumes({ MediaType.APPLICATION_JSON })
	public Response deleteDailyMedicine(DailyMedicine dailyMedicine, @HeaderParam("Authorization") String header) {
		if (!Authentication.isAuthorized(header)) {
			return Response.status(403).build();
		}

		DailyMedicine toAdd = dailyMedicine;

		if (toAdd == null) {
			return Response.status(422).build();
		}
		System.out.println("Got request to delete dailyMedicine: " + gson.toJson(dailyMedicine));
		// if it's a dailyMedicine that is not yet submitted to the database
		if (dailyMedicine.getDailyMedicineID() < 0) {
			// dailyMedicine given is already in database, but with wrong dailyMedicineID
			return Response.status(404).build();

		}

		if (dailyMedicineDao.deleteDailyMedicine(toAdd)) {
			// return dailyMedicine successfully deleted
			return Response.status(200).build();
		} else {
			// return record was already in database, or was wrong format
			return Response.status(404).build();
		}
	}

}
