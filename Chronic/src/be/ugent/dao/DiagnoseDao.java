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
import be.ugent.entitity.Diagnose;

public class DiagnoseDao {
	private MongoDBSingleton dbSingleton = MongoDBSingleton.getInstance();
	private DB db = dbSingleton.getTestdb();
	private Gson gson = new Gson();

	public List<Diagnose> getAllDiagnoses(){
		DBCollection coll = db.getCollection("diagnose");
		DBCursor cursor = coll.find();
		List<Diagnose> list = new ArrayList<Diagnose>();
		while (cursor.hasNext()) {
			DBObject o = cursor.next();
			Diagnose diagnose = new Diagnose();
			diagnose.setDescription(o.get("description")+"");
			diagnose.setName(o.get("name")+"");
			diagnose.setDiagnoseID((int)o.get("diagnoseID"));
			list.add(diagnose);
		}
		return list;
	}
	
	public boolean addDiagnose(Diagnose diagnose){
		
		
		DBCollection collection = db.getCollection("diagnose");
		// convert JSON to DBObject directly
		BasicDBObject bdbo = new BasicDBObject();
		bdbo.put("name", diagnose.getName());
		DBCursor curs = collection.find(bdbo);
		if(curs.count()>0)
			return false;
		Gson genson = new Gson();
		DBObject dbObject = (DBObject) JSON.parse(genson.toJson(diagnose));
		collection.insert(dbObject);

		DBCursor cursorDoc = collection.find();
		while (cursorDoc.hasNext()) {
			System.out.println(cursorDoc.next());
		}

		System.out.println("Done");
		return true;
	}

	public int getNewDiagnoseID() {
		DBCollection coll = db.getCollection("diagnose");
		BasicDBObject whereQuery = new BasicDBObject();
		DBCursor cursor = coll.find(whereQuery);
		int max = 0;
		while(cursor.hasNext()){
			BasicDBObject next = (BasicDBObject) cursor.next();
			if(max < Double.parseDouble(""+next.get("diagnoseID"))){
				max = (int)Double.parseDouble(""+next.get("diagnoseID"));
			}
		}
		return max+1;
	}

	public int getDiagnoseID(Diagnose diagnose) {
		DBCollection coll = db.getCollection("diagnose");
		BasicDBObject whereQuery = new BasicDBObject();
		whereQuery.put("name",diagnose.getName());
		DBCursor cursor = coll.find(whereQuery);
		int id = -1;
		while(cursor.hasNext()){
			BasicDBObject next = (BasicDBObject) cursor.next();
			id = (int)Double.parseDouble(""+next.get("diagnoseID"));
		}
		return id;
	}

	public boolean changeDiagnose(Diagnose diagnose) {

		DBCollection collection = db.getCollection("diagnose");
		// convert JSON to DBObject directly
		BasicDBObject bdbo = new BasicDBObject();
		bdbo.put("diagnoseID", diagnose.getDiagnoseID());
		DBCursor curs = collection.find(bdbo);
		if(curs.count()>1)
			return false;
		Gson genson = new Gson();
		DBObject dbObject = (DBObject) JSON.parse(genson.toJson(diagnose));
		collection.update(bdbo,dbObject);

		DBCursor cursorDoc = collection.find();
		while (cursorDoc.hasNext()) {
			System.out.println(cursorDoc.next());
		}

		System.out.println("Done");
		return true;
	}

	public Diagnose getDiagnose(String name) {
		DBCollection coll = db.getCollection("diagnose");
		BasicDBObject whereQuery = new BasicDBObject();
		whereQuery.put("name", name);
		DBCursor cursor = coll.find(whereQuery);
		Diagnose diagnose = null;
		while (cursor.hasNext()) {
			DBObject o = cursor.next();
			diagnose = gson.fromJson(o.toString(), Diagnose.class);
		}
		return diagnose;
	}

	public boolean deleteDiagnose(Diagnose diagnose) {
		DBCollection collection = db.getCollection("diagnose");
		// convert JSON to DBObject directly
		BasicDBObject bdbo = new BasicDBObject();
		bdbo.put("diagnoseID", diagnose.getDiagnoseID());
		DBCursor curs = collection.find(bdbo);
		if(curs.count()>1){
			System.err.println("Multiple diagnoses in the database have the same id");
			return false;
		}
		
		BasicDBObject document = new BasicDBObject();
		document.put("diagnoseID", diagnose.getDiagnoseID());
		collection.remove(document);
		System.out.println("Done");
		return true;
	}
}
