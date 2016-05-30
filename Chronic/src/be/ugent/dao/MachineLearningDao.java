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

public class MachineLearningDao {
	private MongoDBSingleton dbSingleton = MongoDBSingleton.getInstance();
	private DB db = dbSingleton.getTestdb();
	private Gson gson = new Gson();

	public List<DecisionTree> getAllDecisionTrees(){
		DBCollection coll = db.getCollection("decisionTree");
		DBCursor cursor = coll.find();
		List<DecisionTree> list = new ArrayList<DecisionTree>();
		while (cursor.hasNext()) {
			DBObject o = cursor.next();
			DecisionTree decisionTree = new DecisionTree();
			decisionTree.setType(o.get("type")+"");
			decisionTree.setDokterID((int)o.get("dokterID"));
			decisionTree.setTimestamp(""+o.get("timestamp"));
			decisionTree.setJSON_repr(""+o.get("JSON_repr"));
			list.add(decisionTree);
		}
		return list;
	}
	
//	public boolean addDecisionTree(DecisionTree decisionTree){
//		
//		
//		DBCollection collection = db.getCollection("decisionTree");
//		// convert JSON to DBObject directly
//		BasicDBObject bdbo = new BasicDBObject();
//		bdbo.put("name", decisionTree.getName());
//		DBCursor curs = collection.find(bdbo);
//		if(curs.count()>0)
//			return false;
//		Gson genson = new Gson();
//		DBObject dbObject = (DBObject) JSON.parse(genson.toJson(decisionTree));
//		collection.insert(dbObject);
//
//		DBCursor cursorDoc = collection.find();
//		while (cursorDoc.hasNext()) {
//			System.out.println(cursorDoc.next());
//		}
//
//		System.out.println("Done");
//		return true;
//	}
//
//	public int getNewDecisionTreeID() {
//		DBCollection coll = db.getCollection("decisionTree");
//		BasicDBObject whereQuery = new BasicDBObject();
//		DBCursor cursor = coll.find(whereQuery);
//		int max = 0;
//		while(cursor.hasNext()){
//			BasicDBObject next = (BasicDBObject) cursor.next();
//			if(max < Double.parseDouble(""+next.get("decisionTreeID"))){
//				max = (int)Double.parseDouble(""+next.get("decisionTreeID"));
//			}
//		}
//		return max+1;
//	}
//
//	public int getDecisionTreeID(DecisionTree decisionTree) {
//		DBCollection coll = db.getCollection("decisionTree");
//		BasicDBObject whereQuery = new BasicDBObject();
//		whereQuery.put("name",decisionTree.getName());
//		DBCursor cursor = coll.find(whereQuery);
//		int id = -1;
//		while(cursor.hasNext()){
//			BasicDBObject next = (BasicDBObject) cursor.next();
//			id = (int)Double.parseDouble(""+next.get("decisionTreeID"));
//		}
//		return id;
//	}
//
//	public boolean changeDecisionTree(DecisionTree decisionTree) {
//
//		DBCollection collection = db.getCollection("decisionTree");
//		// convert JSON to DBObject directly
//		BasicDBObject bdbo = new BasicDBObject();
//		bdbo.put("decisionTreeID", decisionTree.getDecisionTreeID());
//		DBCursor curs = collection.find(bdbo);
//		if(curs.count()>1)
//			return false;
//		Gson genson = new Gson();
//		DBObject dbObject = (DBObject) JSON.parse(genson.toJson(decisionTree));
//		collection.update(bdbo,dbObject);
//
//		DBCursor cursorDoc = collection.find();
//		while (cursorDoc.hasNext()) {
//			System.out.println(cursorDoc.next());
//		}
//
//		System.out.println("Done");
//		return true;
//	}
//
//	public DecisionTree getDecisionTree(String name) {
//		DBCollection coll = db.getCollection("decisionTree");
//		BasicDBObject whereQuery = new BasicDBObject();
//		whereQuery.put("name", name);
//		DBCursor cursor = coll.find(whereQuery);
//		DecisionTree decisionTree = null;
//		while (cursor.hasNext()) {
//			DBObject o = cursor.next();
//			decisionTree = gson.fromJson(o.toString(), DecisionTree.class);
//		}
//		return decisionTree;
//	}
//
//	public boolean deleteDecisionTree(DecisionTree decisionTree) {
//		DBCollection collection = db.getCollection("decisionTree");
//		// convert JSON to DBObject directly
//		BasicDBObject bdbo = new BasicDBObject();
//		bdbo.put("decisionTreeID", decisionTree.getDecisionTreeID());
//		DBCursor curs = collection.find(bdbo);
//		if(curs.count()>1){
//			System.err.println("Multiple decisionTrees in the database have the same id");
//			return false;
//		}
//		
//		BasicDBObject document = new BasicDBObject();
//		document.put("decisionTreeID", decisionTree.getDecisionTreeID());
//		collection.remove(document);
//		System.out.println("Done");
//		return true;
//	}
}
