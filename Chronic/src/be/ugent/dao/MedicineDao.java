package be.ugent.dao;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.ws.rs.Consumes;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.google.gson.Gson;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;
import com.owlike.genson.Genson;

import be.ugent.Authentication;
import be.ugent.MongoDBSingleton;
import be.ugent.entitity.Drug;
import be.ugent.entitity.Medicine;
import be.ugent.entitity.Patient;
import be.ugent.entitity.Trigger;

public class MedicineDao {
	private MongoDBSingleton dbSingleton = MongoDBSingleton.getInstance();
	private DB db = dbSingleton.getTestdb();

	public List<Medicine> getAllMedicinesForPatient(Patient patient){
		DBCollection coll = db.getCollection("medicine");
		BasicDBObject where = new BasicDBObject();
		where.put("patientID", patient.getPatientID());
		DBCursor cursor = coll.find(where);
		List<Medicine> list = new ArrayList<Medicine>();
		while (cursor.hasNext()) {
			DBObject o = cursor.next();
			Medicine medicine = new Medicine();
			DateFormat df = new SimpleDateFormat("EEE MMM dd kk:mm:ss z yyyy", Locale.ENGLISH);
			Date result = null;
		    try {
				result =  df.parse(""+o.get("date"));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			medicine.setDate(result+"");
			medicine.setDrugID(Integer.parseInt(""+o.get("medicineID")));
			medicine.setMedicineID(Integer.parseInt(""+o.get("medicineID")));
			medicine.setPatientID(patient.getPatientID());
			medicine.setQuantity(Float.parseFloat(""+o.get("quantity")));
			list.add(medicine);
		}
		return list;
	}
	
	public boolean addMedicineForPatient(Medicine medicine){
		DBCollection collection = db.getCollection("medicine");
		// convert JSON to DBObject directly
		BasicDBObject bdbo = new BasicDBObject();
		bdbo.put("medicineID", medicine.getMedicineID());
		DBCursor curs = collection.find(bdbo);
		if(curs.count()>0){
			System.out.println("This medicine record (medicineID) already exists in the database");
			System.err.println("This medicine record (medicineID) already exists in the database");
			return false;
		}
		Genson genson = new Genson();
		DBObject dbObject = (DBObject) JSON.parse(genson.serialize(medicine));
		collection.insert(dbObject);

		DBCursor cursorDoc = collection.find();
		while (cursorDoc.hasNext()) {
			System.out.println(cursorDoc.next());
		}

		System.out.println("Done");
		return true;
	}

	private Gson gson = new Gson();

	//probably unnecessary
	public List<Medicine> getAllMedicines(){
		DBCollection coll = db.getCollection("medicine");
		DBCursor cursor = coll.find();
		List<Medicine> list = new ArrayList<Medicine>();
		while (cursor.hasNext()) {
			DBObject o = cursor.next();
			Medicine medicine = gson.fromJson(o.toString(), Medicine.class);
			list.add(medicine);
		}
		return list;
	}
	
	public boolean addMedicine(Medicine medicine){
		DBCollection collection = db.getCollection("medicine");
		// convert JSON to DBObject directly
		BasicDBObject bdbo = new BasicDBObject();
		bdbo.put("patientID", medicine.getPatientID());
		bdbo.put("drugID", medicine.getDrugID());
		DBCursor curs = collection.find(bdbo);
		if(curs.count()>0)
			return false;
		Genson genson = new Genson();
		DBObject dbObject = (DBObject) JSON.parse(genson.serialize(medicine));
		collection.insert(dbObject);

		DBCursor cursorDoc = collection.find();
		while (cursorDoc.hasNext()) {
			System.out.println(cursorDoc.next());
		}

		System.out.println("Done");
		return true;
	}

	public int getNewMedicineID() {
		DBCollection coll = db.getCollection("medicine");
		BasicDBObject whereQuery = new BasicDBObject();
		DBCursor cursor = coll.find(whereQuery);
		int max = 0;
		while(cursor.hasNext()){
			BasicDBObject next = (BasicDBObject) cursor.next();
			if(max < Double.parseDouble(""+next.get("medicineID"))){
				max = (int)Double.parseDouble(""+next.get("medicineID"));
			}
		}
		return max+1;
	}

	public int getMedicineID(Medicine medicine) {
		DBCollection coll = db.getCollection("medicine");
		BasicDBObject whereQuery = new BasicDBObject();
		whereQuery.put("patientID", medicine.getPatientID());
		whereQuery.put("drugID", medicine.getDrugID());
		DBCursor cursor = coll.find(whereQuery);
		int id = -1;
		while(cursor.hasNext()){
			BasicDBObject next = (BasicDBObject) cursor.next();
			id = (int)Double.parseDouble(""+next.get("medicineID"));
		}
		return id;
	}

	public boolean changeMedicine(Medicine medicine) {

		DBCollection collection = db.getCollection("medicine");
		// convert JSON to DBObject directly
		BasicDBObject bdbo = new BasicDBObject();
		bdbo.put("patientID", medicine.getPatientID());
		bdbo.put("drugID", medicine.getDrugID());
		DBCursor curs = collection.find(bdbo);
		if(curs.count()>1)
			return false;
		Gson genson = new Gson();
		DBObject dbObject = (DBObject) JSON.parse(genson.toJson(medicine));
		collection.update(bdbo,dbObject);

		DBCursor cursorDoc = collection.find();
		while (cursorDoc.hasNext()) {
			System.out.println(cursorDoc.next());
		}

		System.out.println("Done");
		return true;
	}

	public Object getMedicine(int medicineID, int patientID) {
		DBCollection coll = db.getCollection("medicine");
		BasicDBObject whereQuery = new BasicDBObject();
		whereQuery.put("patientID", patientID);
		whereQuery.put("drugID", medicineID);
		DBCursor cursor = coll.find(whereQuery);
		Medicine medicine = null;
		while (cursor.hasNext()) {
			DBObject o = cursor.next();
			medicine = gson.fromJson(o.toString(), Medicine.class);
		}
		return medicine;
	}
	
	public boolean deleteMedicine(Medicine medicine) {
		DBCollection collection = db.getCollection("medicine");
		// convert JSON to DBObject directly
		BasicDBObject bdbo = new BasicDBObject();
		bdbo.put("medicineID", medicine.getMedicineID());
		DBCursor curs = collection.find(bdbo);
		if(curs.count()>1){
			System.err.println("Multiple medicines in the database have the same id");
			return false;
		}
		
		BasicDBObject document = new BasicDBObject();
		document.put("medicineID", medicine.getMedicineID());
		collection.remove(document);
		System.out.println("Done");
		return true;
	}
	
}
