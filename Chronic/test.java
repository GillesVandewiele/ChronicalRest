import com.mongodb.DB;
import com.mongodb.MongoClient;

public class test {

	public static void main(String[] args){

		MongoClient mongoClient = new MongoClient( "localhost" , 9000 );
		// or, to connect to a replica set, with auto-discovery of the primary, supply a seed list of members
		
		DB db = mongoClient.getDB( "mydb" );
		
		
	}
}
