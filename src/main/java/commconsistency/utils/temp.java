package commconsistency.utils;

import org.bson.Document;

import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;

public class temp {
	public static void main2(String[] args) {
		MongoDatabase database = new MongoClient("192.168.1.128", 27017).getDatabase("sourcebase");
		MongoCollection<Document> comments = database.getCollection("comment5");


		FindIterable<Document> iter = comments.find();
		MongoCursor<Document> cursor = iter.iterator();
		int comment_id = 111181;
		while(cursor.hasNext()) {
			if(comment_id%1000==0) {
				System.out.println(comment_id+" is done.");
			}
			Document doc = cursor.next();
			doc.put("comment_id", comment_id);
			BasicDBObject query = new BasicDBObject();
			query.put("_id",doc.get("_id"));
			comments.replaceOne(query, doc);
			comment_id++;
		}
	}
	
	public static void main(String[] args) {
		MongoDatabase database = new MongoClient("39.108.99.24", 27017).getDatabase("sourcebase");
		MongoCollection<Document> comments = database.getCollection("comment_scope");
		MongoCollection<Document> subComments = database.getCollection("sub_comment_scope");
		FindIterable<Document> iter = comments.find();
		MongoCursor<Document> cursor = iter.iterator();
		while(cursor.hasNext()) {
			Document doc = cursor.next();
			Document obj = new Document();
			obj.put("project", doc.get("project"));
			obj.put("commit_id", doc.get("commit_id"));
			obj.put("class_name", doc.get("class_name"));
			obj.put("comment_id", doc.get("comment_id"));
			obj.put("isverify", false);
			subComments.insertOne(obj);
		}
		
	}
	

}
