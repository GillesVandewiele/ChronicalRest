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
import be.ugent.entitity.DailyMedicine;

public class DailyMedicineDao {
	private MongoDBSingleton dbSingleton = MongoDBSingleton.getInstance();
	private DB db = dbSingleton.getTestdb();
	private Gson gson = new Gson();

	public List<DailyMedicine> getAllDailyMedicines(){
		DBCollection coll = db.getCollection("dailyMedicine");
		DBCursor cursor = coll.find();
		List<DailyMedicine> list = new ArrayList<DailyMedicine>();
		while (cursor.hasNext()) {
			DBObject o = cursor.next();
			DailyMedicine dailyMedicine = new DailyMedicine();
			dailyMedicine.setDailyMedicineID(Integer.parseInt(o.get("dailyMedicineID")+""));
			dailyMedicine.setDailyMedicineID((int)o.get("dailyMedicineID"));
			list.add(dailyMedicine);
		}
		return list;
	}
	
	public boolean addDailyMedicine(DailyMedicine dailyMedicine){
		
		
		DBCollection collection = db.getCollection("dailyMedicine");
		// convert JSON to DBObject directly
		BasicDBObject bdbo = new BasicDBObject();
		bdbo.put("medicineID", dailyMedicine.getMedicineID());
		DBCursor curs = collection.find(bdbo);
		if(curs.count()>0)
			return false;
		Gson genson = new Gson();
		DBObject dbObject = (DBObject) JSON.parse(genson.toJson(dailyMedicine));
		collection.insert(dbObject);

		DBCursor cursorDoc = collection.find();
		while (cursorDoc.hasNext()) {
			System.out.println(cursorDoc.next());
		}

		System.out.println("Done");
		return true;
	}

	public int getNewDailyMedicineID() {
		DBCollection coll = db.getCollection("dailyMedicine");
		BasicDBObject whereQuery = new BasicDBObject();
		DBCursor cursor = coll.find(whereQuery);
		int max = 0;
		while(cursor.hasNext()){
			BasicDBObject next = (BasicDBObject) cursor.next();
			if(max < Double.parseDouble(""+next.get("dailyMedicineID"))){
				max = (int)Double.parseDouble(""+next.get("dailyMedicineID"));
			}
		}
		return max+1;
	}

	public int getDailyMedicineID(DailyMedicine dailyMedicine) {
		DBCollection coll = db.getCollection("dailyMedicine");
		BasicDBObject whereQuery = new BasicDBObject();
		whereQuery.put("medicineID",dailyMedicine.getMedicineID());
		DBCursor cursor = coll.find(whereQuery);
		int id = -1;
		while(cursor.hasNext()){
			BasicDBObject next = (BasicDBObject) cursor.next();
			id = (int)Double.parseDouble(""+next.get("dailyMedicineID"));
		}
		return id;
	}

	public boolean changeDailyMedicine(DailyMedicine dailyMedicine) {

		DBCollection collection = db.getCollection("dailyMedicine");
		// convert JSON to DBObject directly
		BasicDBObject bdbo = new BasicDBObject();
		bdbo.put("dailyMedicineID", dailyMedicine.getDailyMedicineID());
		DBCursor curs = collection.find(bdbo);
		if(curs.count()>1)
			return false;
		Gson genson = new Gson();
		DBObject dbObject = (DBObject) JSON.parse(genson.toJson(dailyMedicine));
		collection.update(bdbo,dbObject);

		DBCursor cursorDoc = collection.find();
		while (cursorDoc.hasNext()) {
			System.out.println(cursorDoc.next());
		}

		System.out.println("Done");
		return true;
	}

	public DailyMedicine getDailyMedicine(int medicineID) {
		DBCollection coll = db.getCollection("dailyMedicine");
		BasicDBObject whereQuery = new BasicDBObject();
		whereQuery.put("medicineID", medicineID);
		DBCursor cursor = coll.find(whereQuery);
		DailyMedicine dailyMedicine = null;
		while (cursor.hasNext()) {
			DBObject o = cursor.next();
			dailyMedicine = gson.fromJson(o.toString(), DailyMedicine.class);
		}
		return dailyMedicine;
	}

	public boolean deleteDailyMedicine(DailyMedicine dailyMedicine) {
		DBCollection collection = db.getCollection("dailyMedicine");
		// convert JSON to DBObject directly
		BasicDBObject bdbo = new BasicDBObject();
		bdbo.put("dailyMedicineID", dailyMedicine.getDailyMedicineID());
		DBCursor curs = collection.find(bdbo);
		if(curs.count()>1){
			System.err.println("Multiple dailyMedicines in the database have the same id");
			return false;
		}
		
		BasicDBObject document = new BasicDBObject();
		document.put("dailyMedicineID", dailyMedicine.getDailyMedicineID());
		collection.remove(document);
		System.out.println("Done");
		return true;
	}
}
