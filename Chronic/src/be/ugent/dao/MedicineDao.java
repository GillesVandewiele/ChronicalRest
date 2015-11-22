package be.ugent.dao;

import java.util.ArrayList;
import java.util.List;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;
import com.owlike.genson.Genson;

import be.ugent.MongoDBSingleton;
import be.ugent.entitity.Drug;
import be.ugent.entitity.Medicine;
import be.ugent.entitity.Patient;

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
			medicine.setDate(""+o.get("date"));
			medicine.setDrugID(Integer.parseInt(""+o.get("drugID")));
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
}
