package commconsistency.utils;

import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;

public class SubCommentEntryGenerator {
	
	public static void main(String[] args) {
		MongoDatabase database = new MongoClient("192.168.1.60", 27017).getDatabase("sourcebase");
		MongoCollection<Document> comments = database.getCollection("comment");
		MongoCollection<Document> subComments = database.getCollection("sub_comment");
		
		int count = 0;
		MongoCursor<Document> cursor = comments.find().iterator();
		while(cursor.hasNext()) {
			Document comment = cursor.next();
			Document document = new Document();
			document.put("comment_id", comment.get("comment_id"));
			document.put("project", comment.get("project"));
			document.put("class_name", comment.get("class_name"));
			document.put("isverify", false);
			document.put("verify_type", "unknown");
			document.put("comment_type", "unknown");
			document.put("filter1", false);
			document.put("filter2", false);
			subComments.insertOne(document);
			count++;
			if(count%1000==0) {
				System.out.println(count+" is done.");
			}
		}
		
	}
	

}
