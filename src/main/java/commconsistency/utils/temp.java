package commconsistency.utils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.bson.Document;

import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;

public class temp {
	public static void main(String[] args) throws IOException {
		MongoDatabase database = new MongoClient("192.168.1.60", 27017).getDatabase("scopebase");
		MongoCollection<Document> rScopeComments = database.getCollection("r_comment_scope");
		
		FindIterable<Document> iter = rScopeComments.find();
		MongoCursor<Document> cursor = iter.iterator();
		List<String> rIdList = new ArrayList<String>();
		while (cursor.hasNext()) {
			Document doc = cursor.next();
			int commentID = doc.getInteger("comment_id");
			rIdList.add(commentID+"");
		}
		FileUtils.writeLines(new File("file/r_commentidlist.txt"), rIdList);
	}
	

}
