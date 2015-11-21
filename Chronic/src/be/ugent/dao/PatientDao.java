package be.ugent.dao;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;
import com.owlike.genson.Genson;

import be.ugent.MongoDBSingleton;
import be.ugent.entitity.Patient;

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
			patient.setEmployed((boolean)object.get("isEmployed"));
			patient.setFirstName(firstName);
			patient.setLastName(lastName);
			patient.setPatientID((int)Double.parseDouble(""+object.get("patientID")));
			patient.setMale((boolean)object.get("isMale"));
			
			return patient;
		}else{
			return null;
		}
	}
	
	public boolean idExists(int id){
		DBCollection coll = db.getCollection("patient");
		BasicDBObject whereQuery = new BasicDBObject();
		whereQuery.put("patientID", id);
		DBCursor cursor = coll.find(whereQuery);
		if(cursor.count()>=1){
			return true;
		}
		return false;
	}
	
	public int getNewId(){
		DBCollection coll = db.getCollection("patient");
		BasicDBObject whereQuery = new BasicDBObject();
		DBCursor cursor = coll.find(whereQuery);
		int max = 0;
		while(cursor.hasNext()){
			BasicDBObject next = (BasicDBObject) cursor.next();
			if(max < Double.parseDouble(""+next.get("patientID"))){
				max = (int)Double.parseDouble(""+next.get("patientID"));
			}
		}
		return max+1;
	}

	public boolean storePatient(Patient patient){
		if(isValidUser(patient)){
//			doStore
			Genson genson = new Genson();
			db.getCollection("patient").insert((BasicDBObject)JSON.parse(genson.serialize(patient)));
//			System.out.println("Test storing: "+genson.serialize(patient));
			return true;
		}
		else{
			System.out.println("Error storing patient : Not a valid Patient object to store");
			System.err.println("Error storing patient : Not a valid Patient object to store");
			return false;
		}
			
	}

	private boolean isValidUser(Patient patient) {
		DBCollection coll = db.getCollection("patient");
		BasicDBObject whereQuery = new BasicDBObject();
		whereQuery.put("patientID", patient.getPatientID());
		DBCursor cursor = coll.find(whereQuery);
		if(cursor.count()>=1){
			System.out.println("Already a patient with this patientID");
			return false;
		}
		whereQuery.clear();
		whereQuery.put("email", patient.getEmail());
		cursor = coll.find(whereQuery);
		if(cursor.count()>=1){
			System.out.println("already a patient with this username in the dabase");
			return false;
		}
		if(patient.getPatientID() < 0 || patient.getEmail() == null || patient.getEmail().equals("") || patient.getFirstName() == null || patient.getFirstName().isEmpty()  || patient.getLastName() == null || patient.getLastName().isEmpty())
			return false;
		return true;
		
	}
}
