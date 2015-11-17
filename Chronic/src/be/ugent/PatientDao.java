package be.ugent;

import java.util.Date;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;

public class PatientDao {
	private MongoDBSingleton dbSingleton = MongoDBSingleton.getInstance();
	private DB db = dbSingleton.getTestdb();

	public Patient getPatient(String firstName, String lastName) {
		Patient patient = new Patient();
		DBCollection coll = db.getCollection("patient");
		BasicDBObject whereQuery = new BasicDBObject();
		whereQuery.put("firstName", firstName);
		whereQuery.put("lastName", lastName);
		DBObject object= coll.findOne(whereQuery);
		
		System.out.println(object+"");
		if(object != null){
//			patient.setBirthDate(new Date(""+object.get("birthDate")));
			patient.setEmployed((boolean)object.get("employed"));
			patient.setFirstName(firstName);
			patient.setLastName(lastName);
			patient.setId((int)Double.parseDouble(""+object.get("id")));
			patient.setMale((boolean)object.get("isMale"));
			return patient;
		}else{
			return null;
		}
	}

}
