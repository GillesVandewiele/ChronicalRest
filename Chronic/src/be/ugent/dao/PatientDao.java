package be.ugent.dao;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;

import com.google.gson.Gson;
import com.mongodb.BasicDBObject;
import com.mongodb.Cursor;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;

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
		DBObject object = coll.findOne(whereQuery);

		System.out.println(object + "");
		if (object != null) {
			Gson gson = new Gson();
			patient = gson.fromJson(object.toString(), Patient.class);

			return patient;
		} else {
			return null;
		}
	}
	
	public Patient getPatient(int patientID) {
		Patient patient = new Patient();
		DBCollection coll = db.getCollection("patient");
		BasicDBObject whereQuery = new BasicDBObject();
		whereQuery.put("patientID", patientID);
		DBObject object = coll.findOne(whereQuery);

		System.out.println(object + "");
		if (object != null) {
			Gson gson = new Gson();
			patient = gson.fromJson(object.toString(), Patient.class);

			return patient;
		} else {
			return null;
		}
	}

	public Patient getPatient(String email) {
		Patient patient = new Patient();
		DBCollection coll = db.getCollection("patient");
		BasicDBObject whereQuery = new BasicDBObject();
		whereQuery.put("email", email);
		DBObject object = coll.findOne(whereQuery);
		if (object != null) {
			Gson gson = new Gson();
			patient = gson.fromJson(object.toString(), Patient.class);
			return patient;
		} else {
			return null;
		}
	}

	public boolean idExists(int id) {
		DBCollection coll = db.getCollection("patient");
		BasicDBObject whereQuery = new BasicDBObject();
		whereQuery.put("patientID", id);
		DBCursor cursor = coll.find(whereQuery);
		if (cursor.count() >= 1) {
			return true;
		}
		return false;
	}

	public int getNewId() {
		DBCollection coll = db.getCollection("patient");
		BasicDBObject whereQuery = new BasicDBObject();
		DBCursor cursor = coll.find(whereQuery);
		int max = 0;
		while (cursor.hasNext()) {
			BasicDBObject next = (BasicDBObject) cursor.next();
			if (max < Double.parseDouble("" + next.get("patientID"))) {
				max = (int) Double.parseDouble("" + next.get("patientID"));
			}
		}
		return max + 1;
	}

	public String getSemantics(String identity) {
		return "@prefix ex: " + "<http://example.org/>.\n" + identity + " a ex:Location.\n";
	}

	public boolean storePatient(Patient patient) {
		if (isValidUser(patient)) {
			// doStore
			db.getCollection("patient").insert((BasicDBObject) JSON.parse(patient.toString()));
			//
			return true;
		} else {
			patient.setPatientID(getNewId());
			if (isValidUser(patient)) {
				db.getCollection("patient").insert((BasicDBObject) JSON.parse(patient.toString()));
				//
				return true;
			}
			System.out.println("Error storing patient : Not a valid Patient object to store");
			System.err.println("Error storing patient : Not a valid Patient object to store");
			return false;
		}

	}

	private boolean isValidUser(Patient patient) {
		DBCollection coll = db.getCollection("patient");
		BasicDBObject whereQuery = new BasicDBObject();
		if (patient == null) {
			System.err.println("Patient == null to chec if isValidUser");
			return false;
		}else if(patient.getPatientID() <1){
			return false;
		}else if (patient.getPatientID() >= 1) {
			whereQuery.put("patientID", patient.getPatientID());
			DBCursor cursor = coll.find(whereQuery);
			if (cursor.count() >= 1) {
				System.out.println("More then one patient with this patientID");
				return false;
			}
		}

		whereQuery.clear();
		whereQuery.put("email", patient.getEmail());
		DBCursor cursor = coll.find(whereQuery);
		if (cursor.count() >= 1) {
			System.out.println("already a patient with this username in the dabase");
			return false;
		}
		if (patient.getPatientID() < 0 || patient.getEmail() == null || patient.getEmail().equals("")
				|| patient.getFirstName() == null || patient.getFirstName().isEmpty() || patient.getLastName() == null
				|| patient.getLastName().isEmpty())
			return false;
		return true;

	}

	public Patient getPatientFromHeader(String header) {
		byte[] decoded = Base64.getDecoder().decode(header.split(" ")[1]);
		String decodedString = new String(decoded, StandardCharsets.UTF_8);
		// System.out.println("Decoded: " + decodedString);

		String requestEmail = decodedString.split(":")[0];

		Patient patient = new Patient();
		DBCollection coll = db.getCollection("patient");
		BasicDBObject whereQuery = new BasicDBObject();

		whereQuery.put("email", requestEmail);
		DBObject object = coll.findOne(whereQuery);
		if (object != null) {
			Gson gson = new Gson();
			patient = gson.fromJson(object.toString(), Patient.class);
			return patient;
		} else
			return null;
	}

	public Patient getPatienFromId(String patientID) {
		Patient patient = new Patient();
		DBCollection coll = db.getCollection("patient");
		BasicDBObject whereQuery = new BasicDBObject();
		whereQuery.put("patientID", Integer.parseInt(patientID));
		DBObject object = coll.findOne(whereQuery);
		if (object != null) {
			Gson gson = new Gson();
			patient = gson.fromJson(object.toString(), Patient.class);
			return patient;
		} else {
			return null;
		}
	}

	public Integer[] getAllPatients() {
		ArrayList<Integer> ids = new ArrayList<>();
		DBCollection coll = db.getCollection("patient");
		BasicDBObject whereQuery = new BasicDBObject();
		Cursor cursor = coll.find(whereQuery);
		while (cursor.hasNext()) {
			DBObject o = cursor.next();
			Gson gson = new Gson();
			Patient patient = gson.fromJson(o.toString(), Patient.class);
			ids.add(patient.getPatientID());
		}
		Integer[] temp = new Integer[ids.size()];
		return ids.toArray(temp);
	}

	private boolean isValidUserToChange(Patient patient) {
		DBCollection coll = db.getCollection("patient");
		BasicDBObject whereQuery = new BasicDBObject();
		if (patient == null) {
			System.err.println("Patient == null to chec if isValidUser");
			return false;
		}else if(patient.getPatientID() <1){
			return false;
		}else if (patient.getPatientID() >= 1) {
			whereQuery.put("patientID", patient.getPatientID());
			DBCursor cursor = coll.find(whereQuery);
			if (cursor.count() > 1) {
				System.out.println("More then one patient with this patientID");
				return false;
			}
		}

		if (patient.getPatientID() < 0 || patient.getEmail() == null || patient.getEmail().equals(""))
			return false;
		return true;

	}
	
	public boolean updatePatient(Patient toAdd) {
		if (isValidUserToChange(toAdd)) {
			// doStore
			
			DBCollection collection = db.getCollection("patient");
			// convert JSON to DBObject directly
			BasicDBObject bdbo = new BasicDBObject();
			bdbo.put("patientID", toAdd.getPatientID());
			DBCursor curs = collection.find(bdbo);
			if(curs.count()>1)
				return false;
			Gson genson = new Gson();
			Patient oud = genson.fromJson(curs.next().toString(), Patient.class);
			toAdd.setFirstName(oud.getFirstName());
			toAdd.setLastName(oud.getLastName());
			DBObject dbObject = (DBObject) JSON.parse(genson.toJson(toAdd));
			collection.update(bdbo,dbObject);
			
			return true;
		} else {
			System.out.println("Error storing patient : Not a valid Patient object to store");
			System.err.println("Error storing patient : Not a valid Patient object to store");
			return false;
		}

	}

}
