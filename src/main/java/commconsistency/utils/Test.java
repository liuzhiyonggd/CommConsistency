package commconsistency.utils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.bson.Document;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;

public class Test {
	
//	public static void main(String[] args) throws IOException {
//		MongoCollection<Document> subComments = MongoConnect.getClient().getDatabase("sourcebase").getCollection("sub_comment");
//		
//		List<String> idList = FileUtils.readLines(new File("E:\\注释一致性实验\\数据\\filterID.txt"),"UTF-8");
//		for(String str:idList) {
//			int commentID = Integer.parseInt(str);
//			Document query = new Document();
//			query.put("comment_id", commentID);
//			Document doc = subComments.find(query).first();
//			doc.put("filter1", false);
//			subComments.replaceOne(query, doc);
//		}
//		
//		Document query = new Document();
//		query.put("filter2", true);
//		query.put("filter1", true);
//		MongoCursor<Document> cursor = subComments.find(query).iterator();
//		List<String> commentIDList = new ArrayList<String>();
//		while(cursor.hasNext()) {
//			Document doc = cursor.next();
//			commentIDList.add(doc.getInteger("comment_id")+"");
//		}
//		FileUtils.writeLines(new File("e:/filterID.txt"), commentIDList);
//		
//	}
	
//	public static void main(String[] args) throws IOException {
//		List<String> idList = FileUtils.readLines(new File("E:\\注释一致性实验\\数据\\id.txt"),"UTF-8");
//		List<String> filterList = FileUtils.readLines(new File("E:\\注释一致性实验\\数据\\filterID.txt"),"UTF-8");
//		
//		Set<String> filterSet = new HashSet<String>();
//		for(String str:filterList) {
//			filterSet.add(str);
//		}
//		List<String> subList = new ArrayList<String>();
//		for(String str:idList) {
//			if(filterSet.contains(str)) {
//				subList.add(str);
//			}
//		}
//		
//		
//		FileUtils.writeLines(new File("E:\\注释一致性实验\\数据\\subid.txt"), subList);
//	}
	
	public static void main(String[] args) throws IOException {
		List<String> dataList = FileUtils.readLines(new File("E:\\注释一致性实验\\数据\\data.txt"),"UTF-8");
		List<String> header = FileUtils.readLines(new File("E:\\注释一致性实验\\数据\\header.txt"),"UTF-8");
		List<String> output = new ArrayList<String>();
		output.addAll(header);
		for(String str:dataList) {
			String[] temps = str.split(",");
			if(temps[1].equals("4")) {
				output.add(str);
			}
		}
		FileUtils.writeLines(new File("E:\\注释一致性实验\\数据\\data_4.arff"), output);
	}

}
