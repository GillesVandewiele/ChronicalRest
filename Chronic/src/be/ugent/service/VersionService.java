package be.ugent.service;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.google.gson.Gson;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

import be.ugent.MongoDBSingleton;
import be.ugent.dao.VersionDao;
import be.ugent.entitity.Drug;
import be.ugent.entitity.Version;

@Path("/VersionService")
public class VersionService {
	VersionDao versionDao = new VersionDao();
	@GET
	@Path("/version")
	@Produces({ MediaType.APPLICATION_JSON })
	public Response getVersion() {
		
		return Response.ok(versionDao.getLatestVersion()).build();
	}
	
	@GET
	@Path("/verification")
	@Produces({ MediaType.APPLICATION_JSON })
	public Response getVerificationServices() {
		MongoDBSingleton dbSingleton = MongoDBSingleton.getInstance();
		DB db = dbSingleton.getTestdb();
		Gson gson = new Gson();

		
		DBCollection coll = db.getCollection("verificationCode");
		DBCursor cursor = coll.find();
		List<String> list = new ArrayList<String>();
		while (cursor.hasNext()) {
			DBObject o = cursor.next();
			String verifiactionCode = o.get("code")+"";
			list.add(verifiactionCode);
		}
		return Response.ok(gson.toJson(list)).build();
		
		
	}
	
	
	@POST
	@Path("/version")
	@Produces({MediaType.APPLICATION_JSON})
	public Response addVersion(Version version){
		if(versionDao.addVersion(version))
			return Response.status(201).build();
		else
			return Response.status(409).build();
	}
	
	@DELETE
	@Path("/version")
	@Produces({MediaType.APPLICATION_JSON})
	public Response removeVersion(Version version){
		if(versionDao.removeVersion(version))
			return Response.status(200).build();
		else
			return Response.status(409).build();
	}

}
