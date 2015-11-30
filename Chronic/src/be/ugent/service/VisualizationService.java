package be.ugent.service;

import java.io.IOException;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.github.jsonldjava.core.JsonLdError;
import com.github.jsonldjava.core.JsonLdProcessor;
import com.github.jsonldjava.utils.JsonUtils;
import com.google.gson.Gson;
import com.mongodb.util.JSON;


@Path("/VisualizationService")
public class VisualizationService {

	@GET
	@Path("/sensors")
	@Produces({ MediaType.APPLICATION_JSON })
	public Response getAllSensors( @QueryParam("patientID") String patientID) {
			//TODO: better catch blocks and return states
		
		
             String context = "\"@context\": {"+
             "\"name\": \"http://example.org/name\","+
             "\"semanticName\": \"http://example.org/semanticName\","+
             "\"url\": \"http://example.org/url\""+
             "}";
             
//           \"description\": \"Test\"
             String values = "\"name\":"+"\"Hoofdpijn Intensiteiten\" ,"+
                             "\"semanticName\": \"http://example.org/HeadacheIntensitySensor\","+
                             "\"url\": \""+ "headache_intensities\"";
             String output = "{"+context+","+values +"}";
            
             Object compact;
			try {
				compact = JsonLdProcessor.expand(JSON.parse(output));
	             System.out.println(compact+"");
	             return Response.ok(JsonUtils.toPrettyString(compact)).build();
			} catch (JsonLdError e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JsonGenerationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return Response.status(422).build();
	}
}
