package be.ugent.service;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import be.ugent.dao.VersionDao;
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
	
	@POST
	@Path("/version")
	@Produces({MediaType.APPLICATION_JSON})
	public Response addVersion(Version version){
		if(versionDao.addDrug(version))
			return Response.status(201).build();
		else
			return Response.status(409).build();
	}

}
