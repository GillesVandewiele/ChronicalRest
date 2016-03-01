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
import be.ugent.entitity.Drug;
import be.ugent.entitity.Version;

public class VersionDao {
	private MongoDBSingleton dbSingleton = MongoDBSingleton.getInstance();
	private DB db = dbSingleton.getTestdb();

	public String getLatestVersion(){
		DBCollection coll = db.getCollection("version");
		DBCursor cursor = coll.find();
		String max = "";
		while (cursor.hasNext()) {
			DBObject o = cursor.next();
			Version version = new Version();
			version.setDescription(o.get("description")+"");
			version.setVersionID(""+o.get("versionID"));
			if(max.compareTo(version.getVersionID()) <= 0){
				max = version.getVersionID();
			}
		}
		return max;
	}
	
	public boolean addDrug(Version version){
		
		
		DBCollection collection = db.getCollection("version");
		// convert JSON to DBObject directly
		BasicDBObject bdbo = new BasicDBObject();
		bdbo.put("versionID", version.getVersionID());
		DBCursor curs = collection.find(bdbo);
		if(curs.count()>0)
			return false;
		Gson genson = new Gson();
		DBObject dbObject = (DBObject) JSON.parse(genson.toJson(version));
		collection.insert(dbObject);

		DBCursor cursorDoc = collection.find();
		while (cursorDoc.hasNext()) {
			System.out.println(cursorDoc.next());
		}

		System.out.println("Done");
		return true;
	}

}
