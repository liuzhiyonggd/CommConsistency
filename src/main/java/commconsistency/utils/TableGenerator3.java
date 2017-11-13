package commconsistency.utils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;

public class TableGenerator3 {
	public static void generateSubCommentScopeTable() {
		MongoDatabase database = new MongoClient("192.168.2.168", 27017).getDatabase("scopebase");
		MongoCollection<Document> comments = database.getCollection("comment3");
		MongoCollection<Document> subComments = database.getCollection("sub_comment_entry4");
//		FindIterable<Document> iter = comments.find();
//		MongoCursor<Document> cursor = iter.iterator();
//		while (cursor.hasNext()) {
//			Document doc = cursor.next();
		List<String> fileLines = new ArrayList<String>();
		try {
			fileLines = FileUtils.readLines(new File("file/false1_rq2_4000.txt"),"UTF-8");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Document query = new Document();
		for(String str:fileLines) {
			query.put("comment_id", Integer.parseInt(str));
			Document doc = comments.find(query).first();
			Document subComment = new Document();
			subComment.append("comment_id", doc.getInteger("comment_id"));
			subComment.append("project", doc.getString("project"));
			subComment.append("class_name", doc.getString("class_name"));
			subComment.append("type",doc.getString("type"));
			subComment.append("isverify", false);
			subComments.insertOne(subComment);
		}
		
	}
	
	public static void main(String[] args) {
		generateSubCommentScopeTable();
		generateSubCommentScopeTable();
		generateSubCommentScopeTable();
	}
}
