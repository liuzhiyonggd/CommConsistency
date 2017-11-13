package commconsistency.domain;

import java.util.List;

import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;

import commconsistency.dao.CommentScopeRepository;
import commconsistency.service.CommentScopeService;

public class Test {
	
	public static void main(String[] args) {
		MongoDatabase db = new MongoClient("192.168.2.168",27017).getDatabase("scopebase");
		MongoDatabase db2 = new MongoClient("192.168.2.168",27017).getDatabase("sourcebase");
		
		MongoCollection<Document> comments = db.getCollection("comment6");
		MongoCollection<Document> comments2 = db2.getCollection("comment6");
		
		MongoCursor<Document> cursor = comments2.find().iterator();
		int count = 0;
		while(cursor.hasNext()){
			count++;
			if(count%10000==0) {
				System.out.println(count+" is done.");
			}
			Document doc = cursor.next();
			comments.insertOne(doc);
		}
	}

}
