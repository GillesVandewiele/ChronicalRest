package be.ugent.service;

import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

import be.ugent.MongoDBSingleton;
import be.ugent.dao.UserDao;
import be.ugent.entitity.User;

@Path("/UserService")
public class UserService {

	UserDao userDao = new UserDao();

	@GET
	@Path("/users")
	@Produces({ MediaType.APPLICATION_JSON })
	public List<Object> getUsers() {
		System.out.println("lel");
		MongoDBSingleton dbSingleton = MongoDBSingleton.getInstance();
		DB db = dbSingleton.getTestdb();
		DBCollection coll = db.getCollection("headache");
		DBCursor cursor = coll.find();
		List<Object> list = new ArrayList<Object>();
		while (cursor.hasNext()) {
			DBObject o = cursor.next();
			User u = new User();
			u.setName(o.get("end") + "");
			list.add(u);
		}
		return list;
	}
}