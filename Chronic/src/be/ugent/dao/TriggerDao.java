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
import be.ugent.entitity.Trigger;

public class TriggerDao {
	private MongoDBSingleton dbSingleton = MongoDBSingleton.getInstance();
	private DB db = dbSingleton.getTestdb();

	public List<Trigger> getAllTriggers(){
		DBCollection coll = db.getCollection("trigger");
		DBCursor cursor = coll.find();
		List<Trigger> list = new ArrayList<Trigger>();
		while (cursor.hasNext()) {
			DBObject o = cursor.next();
			Trigger drug = new Trigger();
			drug.setDescription(o.get("description")+"");
			drug.setName(o.get("name")+"");
			drug.setTriggerID(((int)o.get("triggerID")));
			list.add(drug);
		}
		return list;
	}
	
	public boolean addTrigger(Trigger drug){
		
		
		DBCollection collection = db.getCollection("trigger");
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

	public int getNewTriggerID() {
		DBCollection coll = db.getCollection("trigger");
		BasicDBObject whereQuery = new BasicDBObject();
		DBCursor cursor = coll.find(whereQuery);
		int max = 0;
		while(cursor.hasNext()){
			BasicDBObject next = (BasicDBObject) cursor.next();
			if(max < Double.parseDouble(""+next.get("triggerID"))){
				max = (int)Double.parseDouble(""+next.get("triggerID"));
			}
		}
		return max+1;
	}
}
