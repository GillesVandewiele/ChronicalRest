package be.ugent.dao;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.Gson;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;

import be.ugent.MongoDBSingleton;
import be.ugent.entitity.Headache;
import be.ugent.entitity.Patient;

public class HeadacheDao {
	private MongoDBSingleton dbSingleton = MongoDBSingleton.getInstance();
	private DB db = dbSingleton.getTestdb();
	private Gson gson = new Gson();

	public List<Headache> getAllHeadachesForPatient(int i){
		DBCollection coll = db.getCollection("headache");
		DBObject whereQuery = new BasicDBObject();
		whereQuery.put("patientID", i);
		DBCursor cursor = coll.find(whereQuery);
		List<Headache> list = new ArrayList<Headache>();
		while (cursor.hasNext()) {
			DBObject o = cursor.next();
			System.out.println("JSON object:"+o.toString());
			Headache headache = gson.fromJson(o.toString(), Headache.class);
			list.add(headache);
		}
		return list;
	}
	
	public String getSemantics(String identity){
		return  "@prefix ex: "+"<http://example.org/>.\n" +
				identity + " a ex:Sensor.\n";
	}
	
	public boolean addHeadacheForPatient(Patient patient, Headache headache){
		DBCollection collection = db.getCollection("headache");
		// convert JSON to DBObject directly
		BasicDBObject bdbo = new BasicDBObject();
		bdbo.put("headacheID", headache.getHeadacheID());
		DBCursor curs = collection.find(bdbo);
		if(curs.count()>0)
			return false;
		bdbo = new BasicDBObject();
		System.out.println("Headache end:"+headache.getEnd());
		bdbo.put("end", headache.getEnd());
		curs = collection.find(bdbo);
		if(curs.count()>0)
			return false;
		Gson genson = new Gson();
		DBObject dbObject = (DBObject) JSON.parse(genson.toJson(headache));
		collection.insert(dbObject);
		
		DBCursor cursorDoc = collection.find();
		while (cursorDoc.hasNext()) {
			System.out.println(cursorDoc.next());
		}

		System.out.println("Done");
		return true;
	}
	

	public int getNewHeadacheID() {
		DBCollection coll = db.getCollection("headache");
		BasicDBObject whereQuery = new BasicDBObject();
		DBCursor cursor = coll.find(whereQuery);
		int max = 0;
		while(cursor.hasNext()){
			BasicDBObject next = (BasicDBObject) cursor.next();
			if(max < Double.parseDouble(""+next.get("headacheID"))){
				max = (int)Double.parseDouble(""+next.get("headacheID"));
			}
		}
		return max+1;
	}

	public int getHeadacheID(Headache headache) {
		DBCollection coll = db.getCollection("headache");
		BasicDBObject whereQuery = new BasicDBObject();
		whereQuery.put("end",headache.getEnd());
		DBCursor cursor = coll.find(whereQuery);
		int id = -1;
		while(cursor.hasNext()){
			BasicDBObject next = (BasicDBObject) cursor.next();
			id = (int)Double.parseDouble(""+next.get("headacheID"));
		}
		return id;
	}

	public boolean updateHeadacheForPatient(Patient patient, Headache headache) {
		DBCollection collection = db.getCollection("headache");
		// convert JSON to DBObject directly
		DBObject bdbo = new BasicDBObject();
		bdbo.put("headacheID", headache.getHeadacheID());
		DBCursor curs = collection.find(bdbo);
		if(curs.count()>1)
			return false;
		DBObject obj = collection.findOne(bdbo);
		
		Gson genson = new Gson();
		DBObject dbObject = (DBObject) JSON.parse(genson.toJson(headache));
		collection.update(obj,dbObject);		
		return true;
	}

	public Headache getHeadache(int headacheID) {
		DBCollection coll = db.getCollection("headache");
		DBObject whereQuery = new BasicDBObject();
		whereQuery.put("headacheID", headacheID);
		DBCursor cursor = coll.find(whereQuery);
		if(cursor.count()>1)
			return null;
		Headache headache = null;
		while (cursor.hasNext()) {
			DBObject o = cursor.next();
			headache = gson.fromJson(o.toString(), Headache.class);
		}
		return headache;
	}
}
