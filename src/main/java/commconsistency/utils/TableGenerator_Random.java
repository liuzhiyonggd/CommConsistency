package commconsistency.utils;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class TableGenerator_Random {
	
	public static void randomGenerate() {
		MongoDatabase database = new MongoClient("192.168.1.60", 27017).getDatabase("scopebase");
		MongoCollection<Document> scopeComments = database.getCollection("comment_scope");
		MongoCollection<Document> subScopeComments = database.getCollection("sub_comment_scope");
		MongoCollection<Document> rScopeComments = database.getCollection("r_comment_scope");
		MongoCollection<Document> rSubScopeComments = database.getCollection("r_sub_comment_scope");
		
		Random random = new Random();
		int count = 0;
		Set<Integer> idSet = new HashSet<Integer>();
		while(count<10000) {
			int commentID = random.nextInt(720000);
			if(!idSet.contains(commentID)) {
				idSet.add(commentID);
				Document query = new Document();
				query.put("comment_id", commentID);
				FindIterable<Document> iter = scopeComments.find(query);
				if(iter!=null&&iter.first()!=null) {
				    Document scopeComment = iter.first();
				    Document subScopeComment = subScopeComments.find(query).first();
				    rScopeComments.insertOne(scopeComment);
				    rSubScopeComments.insertOne(subScopeComment);
				    count++;
				}
			}
			if(count%1000==0) {
				System.out.println(count + " is done.");
			}
		}
	}
	
	public static void main(String[] args) {
		randomGenerate();
	}

}
