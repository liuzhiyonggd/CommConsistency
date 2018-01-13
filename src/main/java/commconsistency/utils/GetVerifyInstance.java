package commconsistency.utils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;

import commconsistency.dao.ConsistencyVerifyRepository;
import commconsistency.dao.RepositoryFactory;
import commconsistency.domain.ConsistencyVerify;

public class GetVerifyInstance {
	
	public static void main(String[] args) throws IOException {
		
		MongoClient client = MongoConnect.getClient();
		MongoCollection<Document> verifys = client.getDatabase("sourcebase").getCollection("consistency_verify6");
		
		List<String> output = new ArrayList<String>();
		MongoCursor<Document> cursor = verifys.find().iterator();
		while(cursor.hasNext()) {
			Document doc = cursor.next();
			int commentID = doc.getInteger("comment_id");
			String changeReason = doc.getString("change_reason");
			boolean isChange = doc.getBoolean("ischange");
			String userName = doc.getString("username");
			output.add(commentID+","+changeReason+","+userName+","+isChange);
		}
		
		FileUtils.writeLines(new File("file/verifyresult.txt"), output);
	}

}
