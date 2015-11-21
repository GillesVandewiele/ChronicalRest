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
import be.ugent.entitity.Symptom;

public class SymptomDao {
	private MongoDBSingleton dbSingleton = MongoDBSingleton.getInstance();
	private DB db = dbSingleton.getTestdb();

	public List<Symptom> getAllSymptoms(){
		DBCollection coll = db.getCollection("symptom");
		DBCursor cursor = coll.find();
		List<Symptom> list = new ArrayList<Symptom>();
		while (cursor.hasNext()) {
			DBObject o = cursor.next();
			Symptom drug = new Symptom();
			drug.setDescription(o.get("description")+"");
			drug.setName(o.get("name")+"");
			drug.setSymptomID(((int)o.get("symptomID")));
			list.add(drug);
		}
		return list;
	}
	
	public boolean addSymptom(Symptom drug){
		
		
		DBCollection collection = db.getCollection("symptom");
		// convert JSON to DBObject directly
		BasicDBObject bdbo = new BasicDBObject();
		bdbo.put("name", drug.getName());
		DBCursor curs = collection.find(bdbo);
		if(curs.count()>0)
			return false;
		Genson genson = new Genson();
		DBObject dbObject = (DBObject) JSON.parse(genson.serialize(drug));
		collection.insert(dbObject);

		DBCursor cursorDoc = collection.find();
		while (cursorDoc.hasNext()) {
			System.out.println(cursorDoc.next());
		}

		System.out.println("Done");
		return true;
	}

	public int getNewSymptomID() {
		DBCollection coll = db.getCollection("symptom");
		BasicDBObject whereQuery = new BasicDBObject();
		DBCursor cursor = coll.find(whereQuery);
		int max = 0;
		while(cursor.hasNext()){
			BasicDBObject next = (BasicDBObject) cursor.next();
			if(max < Double.parseDouble(""+next.get("symptomID"))){
				max = (int)Double.parseDouble(""+next.get("symptomID"));
			}
		}
		return max+1;
	}
}
