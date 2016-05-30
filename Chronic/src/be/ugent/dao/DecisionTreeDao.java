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
import be.ugent.entitity.DecisionTree;
import be.ugent.entitity.Drug;

public class DecisionTreeDao {
	private MongoDBSingleton dbSingleton = MongoDBSingleton.getInstance();
	private DB db = dbSingleton.getTestdb();
	private Gson gson = new Gson();

	public boolean addDecisionTree(DecisionTree decisionTree){
		
		DBCollection collection = db.getCollection("decisionTree");
		// convert JSON to DBObject directly
		BasicDBObject bdbo = new BasicDBObject();
		bdbo.put("timestamp", decisionTree.getTimestamp());
		bdbo.put("dokterID", decisionTree.getDokterID());
		bdbo.put("type", decisionTree.getType());
		DBCursor curs = collection.find(bdbo);
		if(curs.count()>0)
			return false;
		Gson genson = new Gson();
		DBObject dbObject = (DBObject) JSON.parse(genson.toJson(decisionTree));
		collection.insert(dbObject);

		DBCursor cursorDoc = collection.find();
		while (cursorDoc.hasNext()) {
			System.out.println(cursorDoc.next());
		}

		System.out.println("Done");
		return true;
	}

	public int getNewDecisionTreeID() {
		DBCollection coll = db.getCollection("decisionTree");
		BasicDBObject whereQuery = new BasicDBObject();
		DBCursor cursor = coll.find(whereQuery);
		int max = 0;
		while(cursor.hasNext()){
			BasicDBObject next = (BasicDBObject) cursor.next();
			if(max < Double.parseDouble(""+next.get("decisionTreeID"))){
				max = (int)Double.parseDouble(""+next.get("decisionTreeID"));
			}
		}
		return max+1;
	}

	public DecisionTree getDecisionTree(String timestamp,int dokterID,String type) {
		
		DBCollection coll = db.getCollection("decisionTree");
		BasicDBObject whereQuery = new BasicDBObject();
		whereQuery.put("timestamp", timestamp);
		whereQuery.put("dokterID", dokterID);
		whereQuery.put("type", type);
		DBCursor cursor = coll.find(whereQuery);
		DecisionTree decisionTree = null;
		while (cursor.hasNext()) {
			DBObject o = cursor.next();
			decisionTree = gson.fromJson(o.toString(), DecisionTree.class);
		}
		return decisionTree;
	}

	public boolean deleteDecisionTree(DecisionTree decisionTree) {
		DBCollection collection = db.getCollection("decisionTree");
		// convert JSON to DBObject directly
		BasicDBObject bdbo = new BasicDBObject();
		bdbo.put("timestamp", decisionTree.getTimestamp());
		bdbo.put("dokterID", decisionTree.getDokterID());
		bdbo.put("type", decisionTree.getType());
		DBCursor curs = collection.find(bdbo);
		if(curs.count()>1){
			System.err.println("Multiple decisionTrees in the database have the same id");
			return false;
		}
		
		BasicDBObject document = new BasicDBObject();
		document.put("timestamp", decisionTree.getTimestamp());
		document.put("dokterID", decisionTree.getDokterID());
		document.put("type", decisionTree.getType());
		collection.remove(document);
		System.out.println("Done");
		return true;
	}
	
	public List<DecisionTree> getAllDecisionTrees(int dokterID, String type){
		DBCollection coll = db.getCollection("decisionTree");
		
		BasicDBObject whereQuery = new BasicDBObject();
		if(dokterID!=-1){
			whereQuery.put("dokterID", dokterID);
		}
		if(!type.equals("all")){
			whereQuery.put("type", type);
		}
		DBCursor cursor = coll.find(whereQuery);
		
		List<DecisionTree> list = new ArrayList<DecisionTree>();
		while (cursor.hasNext()) {
			DBObject o = cursor.next();
			DecisionTree decisionTree = new DecisionTree();
			decisionTree.setDokterID(dokterID);
			decisionTree.setType(type);
			list.add(decisionTree);
		}
		return list;
	}
}
