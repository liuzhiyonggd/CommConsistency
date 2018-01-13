package commconsistency.utils;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class TableGenerator_Random {
	
	public static void randomGenerate() throws IOException {
		MongoDatabase database = new MongoClient("192.168.1.60", 27017).getDatabase("scopebase");
		MongoCollection<Document> scopeComments = database.getCollection("comment_scope");
		MongoCollection<Document> subScopeComments = database.getCollection("sub_comment_scope");
		MongoCollection<Document> rScopeComments = database.getCollection("r_comment_scope2");
		MongoCollection<Document> rSubScopeComments = database.getCollection("r_sub_comment_scope2");
		
		List<String> idLines = FileUtils.readLines(new File("file/hadoop_hibernate_jdk_jedit.txt"),"UTF-8");
		int count = 0;
		for(String str:idLines) {
			int commentID = Integer.parseInt(str);
			
				Document query = new Document();
				query.put("comment_id", commentID);
				FindIterable<Document> iter = scopeComments.find(query);
				if(iter!=null&&iter.first()!=null) {
				    Document scopeComment = iter.first();
				    scopeComment.remove("_id");
				    Document subScopeComment = subScopeComments.find(query).first();
				    subScopeComment.remove("_id");
				    rScopeComments.insertOne(scopeComment);
				    rSubScopeComments.insertOne(subScopeComment);
				    count++;
				}
			
			if(count%100==0) {
				System.out.println(count + " is done.");
			}
		}
	}
	
	public static void main(String[] args) throws IOException {
		randomGenerate();
	}

}
