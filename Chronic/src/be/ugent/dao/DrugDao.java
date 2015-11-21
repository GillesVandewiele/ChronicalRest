package be.ugent.dao;

import java.util.ArrayList;
import java.util.List;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;

import be.ugent.MongoDBSingleton;
import be.ugent.entitity.Drug;

public class DrugDao {
	private MongoDBSingleton dbSingleton = MongoDBSingleton.getInstance();
	private DB db = dbSingleton.getTestdb();

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
		DBObject dbObject = (DBObject) JSON.parse(JSON.serialize(drug));
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
}
