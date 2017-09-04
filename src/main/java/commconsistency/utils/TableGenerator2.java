package commconsistency.utils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.commons.io.FileUtils;
import org.bson.Document;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.internal.compiler.lookup.Scope;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import commconsistency.refactor.utils.StringTool;

import com.mongodb.BasicDBObject;
import com.mongodb.Block;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;

public class TableGenerator2 {
	static Logger root = (Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);

	static {
		root.setLevel(Level.INFO);
	}

	public static void insertLocalDB() throws IOException {
		MongoDatabase database = new MongoClient("192.168.2.168", 27017).getDatabase("sourcebase");
		MongoCollection<Document> classes = database.getCollection("class_message2");
		MongoCollection<Document> methodExtractors = database.getCollection("method_extractor");
		List<String> fileList = FileUtils.readLines(new File("e:/1.txt"), "UTF-8");
		int i = 0;
		Integer id = 1;
		while (i < fileList.size()) {
			int count = Integer.parseInt(fileList.get(i));
			List<String> split = StringTool.splitPath(fileList.get(++i));
			String project = split.get(0);
			String commitID = split.get(1);
			String className = split.get(2);
			for (int j = 0; j < count; j++) {
				int oldStartLine = Integer.parseInt(fileList.get(++i));
				int oldEndLine = Integer.parseInt(fileList.get(++i));
				int newStartLine = Integer.parseInt(fileList.get(++i));
				int newEndLine = Integer.parseInt(fileList.get(++i));
				List<Integer> invokeList = new ArrayList<Integer>();
				while (!fileList.get(++i).equals("")) {
					invokeList.add(Integer.parseInt(fileList.get(i)));
				}

				BasicDBObject query = new BasicDBObject();
				query.put("project", project);
				query.put("commit_id", commitID);
				query.put("class_name", className);
				System.out.println(project + " " + commitID + " " + className);
				Document clazz = classes.find(query).first();
				List<Document> newCodeDocs = (List<Document>) clazz.get("new_code");
				List<Document> oldCodeDocs = (List<Document>) clazz.get("old_code");
				List<String> newCodeList = new ArrayList<String>();
				List<String> oldCodeList = new ArrayList<String>();
				for (Document doc : newCodeDocs) {
					newCodeList.add(doc.getString("line"));
				}
				for (Document doc : oldCodeDocs) {
					oldCodeList.add(doc.getString("line"));
				}
				Document methodExtractor = new Document();
				methodExtractor.put("method_extractor_id", id);
				methodExtractor.put("project", project);
				methodExtractor.put("commit_id", commitID);
				methodExtractor.put("class_name", className);
				methodExtractor.put("old_start_line", oldStartLine);
				methodExtractor.put("old_end_line", oldEndLine);
				methodExtractor.put("new_start_line", newStartLine);
				methodExtractor.put("new_end_line", newEndLine);
				methodExtractor.put("invokes", invokeList);
				methodExtractor.put("old_code", oldCodeList);
				methodExtractor.put("new_code", newCodeList);
				methodExtractors.insertOne(methodExtractor);
				id++;
			}
			i++;
		}

	}

	public static void copy() {
		MongoDatabase database1 = new MongoClient("192.168.2.168", 27017).getDatabase("sourcebase");
		MongoCollection<Document> methodExtractors1 = database1.getCollection("method_extractor");

		MongoDatabase database2 = new MongoClient("39.108.99.24", 27017).getDatabase("sourcebase");
		MongoCollection<Document> methodExtractors2 = database2.getCollection("method_extractor");

		MongoCursor<Document> cursor = methodExtractors1.find().iterator();
		int i = 0;
		while (cursor.hasNext()) {
			Document doc = cursor.next();
			methodExtractors2.insertOne(doc);
			System.out.println("copy " + (i++));
		}
	}

	public static void insertSubTable() {

		MongoDatabase database = new MongoClient("39.108.99.24", 27017).getDatabase("sourcebase");
		MongoCollection<Document> methodExtractors = database.getCollection("method_extractor");
		MongoCollection<Document> subMethodExtractors = database.getCollection("submethod_extractor");
		MongoCursor<Document> cursor = methodExtractors.find().iterator();
		while (cursor.hasNext()) {
			Document doc = cursor.next();
			Document subDoc = new Document();
			subDoc.put("method_extractor_id", doc.get("method_extractor_id"));
			subDoc.put("project", doc.get("project"));
			subDoc.put("commit_id", doc.get("commit_id"));
			subDoc.put("class_name", doc.get("class_name"));
			subDoc.put("verify", false);
			subMethodExtractors.insertOne(subDoc);
			System.out.println("sub table insert " + subDoc.getInteger("method_extractor_id"));
		}
	}

	public static void main(String[] args) throws IOException {
		insertLocalDB();
		copy();
		insertSubTable();
	}

}
