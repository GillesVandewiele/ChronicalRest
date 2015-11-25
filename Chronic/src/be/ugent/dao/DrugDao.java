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
import com.owlike.genson.Genson;

import be.ugent.MongoDBSingleton;
import be.ugent.entitity.Drug;

public class DrugDao {
	private MongoDBSingleton dbSingleton = MongoDBSingleton.getInstance();
	private DB db = dbSingleton.getTestdb();
	private Gson gson = new Gson();

	public List<Drug> getAllDrugs(){
		DBCollection coll = db.getCollection("drug");
		DBCursor cursor = coll.find();
		List<Drug> list = new ArrayList<Drug>();
		while (cursor.hasNext()) {
			DBObject o = cursor.next();
			Drug drug = new Drug();
			drug.setDescription(o.get("description")+"");
			drug.setName(o.get("name")+"");
			drug.setDrugID((int)o.get("drugID"));
			list.add(drug);
		}
		return list;
	}
	
	public boolean addDrug(Drug drug){
		
		
		DBCollection collection = db.getCollection("drug");
		// convert JSON to DBObject directly
		BasicDBObject bdbo = new BasicDBObject();
		bdbo.put("name", drug.getName());
		DBCursor curs = collection.find(bdbo);
		if(curs.count()>0)
			return false;
		Gson genson = new Gson();
		DBObject dbObject = (DBObject) JSON.parse(genson.toJson(drug));
		collection.insert(dbObject);

		DBCursor cursorDoc = collection.find();
		while (cursorDoc.hasNext()) {
			System.out.println(cursorDoc.next());
		}

		System.out.println("Done");
		return true;
	}

	public int getNewDrugID() {
		DBCollection coll = db.getCollection("drug");
		BasicDBObject whereQuery = new BasicDBObject();
		DBCursor cursor = coll.find(whereQuery);
		int max = 0;
		while(cursor.hasNext()){
			BasicDBObject next = (BasicDBObject) cursor.next();
			if(max < Double.parseDouble(""+next.get("drugID"))){
				max = (int)Double.parseDouble(""+next.get("drugID"));
			}
		}
		return max+1;
	}

	public int getDrugID(Drug drug) {
		DBCollection coll = db.getCollection("drug");
		BasicDBObject whereQuery = new BasicDBObject();
		whereQuery.put("name",drug.getName());
		DBCursor cursor = coll.find(whereQuery);
		int id = -1;
		while(cursor.hasNext()){
			BasicDBObject next = (BasicDBObject) cursor.next();
			id = (int)Double.parseDouble(""+next.get("drugID"));
		}
		return id;
	}

	public boolean changeDrug(Drug drug) {

		DBCollection collection = db.getCollection("drug");
		// convert JSON to DBObject directly
		BasicDBObject bdbo = new BasicDBObject();
		bdbo.put("drugID", drug.getDrugID());
		DBCursor curs = collection.find(bdbo);
		if(curs.count()>1)
			return false;
		Gson genson = new Gson();
		DBObject dbObject = (DBObject) JSON.parse(genson.toJson(drug));
		collection.update(bdbo,dbObject);

		DBCursor cursorDoc = collection.find();
		while (cursorDoc.hasNext()) {
			System.out.println(cursorDoc.next());
		}

		System.out.println("Done");
		return true;
	}

	public Drug getDrug(String name) {
		DBCollection coll = db.getCollection("drug");
		BasicDBObject whereQuery = new BasicDBObject();
		whereQuery.put("name", name);
		DBCursor cursor = coll.find(whereQuery);
		Drug drug = null;
		while (cursor.hasNext()) {
			DBObject o = cursor.next();
			drug = gson.fromJson(o.toString(), Drug.class);
		}
		return drug;
	}

	public boolean deleteDrug(Drug drug) {
		DBCollection collection = db.getCollection("drug");
		// convert JSON to DBObject directly
		BasicDBObject bdbo = new BasicDBObject();
		bdbo.put("drugID", drug.getDrugID());
		DBCursor curs = collection.find(bdbo);
		if(curs.count()>1){
			System.err.println("Multiple drugs in the database have the same id");
			return false;
		}
		
		BasicDBObject document = new BasicDBObject();
		document.put("drugID", drug.getDrugID());
		collection.remove(document);
		System.out.println("Done");
		return true;
	}
}
