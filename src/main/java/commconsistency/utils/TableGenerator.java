package commconsistency.utils;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;

import org.bson.Document;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;

import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;

public class TableGenerator {
	static Logger root = (Logger) LoggerFactory
	        .getLogger(Logger.ROOT_LOGGER_NAME);

	static {
	    root.setLevel(Level.INFO);
	}
	public static void generateCommentScopeTable(String project) {
		MongoDatabase database = new MongoClient("192.168.1.60", 27017).getDatabase("scopebase");
		MongoCollection<Document> classes = database.getCollection("class_message");
		MongoCollection<Document> comments = database.getCollection("comment");
		MongoCollection<Document> scopeComments = database.getCollection("comment_scope");

		BasicDBObject query = new BasicDBObject();
		query.put("project", project);

		FindIterable<Document> iter = classes.find(query);
		MongoCursor<Document> cursor = iter.iterator();
		while (cursor.hasNext()) {
			Document clazz = cursor.next();
			int classID = clazz.getInteger("class_id");
			List<String> codes = (List<String>) clazz.get("codes");
			StringBuilder sb = new StringBuilder();
			for (String code : codes) {
				sb.append(code).append("\r\n");
			}

			ASTParser astParser = ASTParser.newParser(AST.JLS8);
			astParser.setSource(sb.toString().toCharArray());
			astParser.setKind(ASTParser.K_COMPILATION_UNIT);
			CompilationUnit unit = (CompilationUnit) (astParser.createAST(null));
			if(unit.types()==null||unit.types().size()<=0) {
				continue;
			}
			TypeDeclaration type = (TypeDeclaration)unit.types().get(0);
			
			MethodDeclaration[] methods = type.getMethods();
			
			Document query2 = new Document();
			query2.put("class_id", classID);
			MongoCursor<Document> cursor2 = comments.find(query2).iterator();
			while(cursor2.hasNext()) {
				Document doc = cursor2.next();
				
				int startLine = doc.getInteger("scope_start_line");
				int endLine = doc.getInteger("scope_end_line");
				
				int methodStartLine = 0,methodEndLine=0;
				for(MethodDeclaration method:methods) {
					int t_methodStartLine = unit.getLineNumber(method.getStartPosition());
					int t_methodEndLine = unit.getLineNumber(method.getStartPosition()+method.getLength()-1);
					
					if(t_methodStartLine<=startLine&&t_methodEndLine>=endLine) {
						methodStartLine = t_methodStartLine;
						methodEndLine = t_methodEndLine;
						break;
					}
				}
				
				Document scopeDocument = new Document();
				scopeDocument.put("comment_id", doc.get("comment_id"));
				scopeDocument.put("class_id", doc.get("class_id"));
				scopeDocument.put("project", clazz.get("project"));
				scopeDocument.put("class_name", clazz.get("class_name"));
				scopeDocument.put("type", doc.get("type"));
				scopeDocument.put("method_start_line", methodStartLine);
				scopeDocument.put("method_end_line", methodEndLine);
				scopeDocument.put("scope_start_line", startLine);
				scopeDocument.put("scope_end_line", endLine);
				scopeDocument.put("comment_start_line", doc.get("comment_start_line"));
				scopeDocument.put("comment_end_line", doc.get("comment_end_line"));
				scopeDocument.put("codes", codes);
				List<Integer> verifyScopeEndLine = new ArrayList<Integer>();
				scopeDocument.put("vscope_end_line", verifyScopeEndLine);
				scopeComments.insertOne(scopeDocument);
			}
			

		}

	}
	
	public static void generateSubCommentScopeTable(String project) {
		MongoDatabase database = new MongoClient("192.168.1.60", 27017).getDatabase("scopebase");
		MongoCollection<Document> scopeComments = database.getCollection("comment_scope");
		MongoCollection<Document> subScopeComments = database.getCollection("sub_comment_scope");
		Document query = new Document();
		query.put("project", project);
		FindIterable<Document> iter = scopeComments.find(query);
		MongoCursor<Document> cursor = iter.iterator();
		while (cursor.hasNext()) {
			Document doc = cursor.next();
			Document subScope = new Document();
			subScope.append("comment_id", doc.getInteger("comment_id"));
			subScope.append("project", doc.getString("project"));
			subScope.append("class_name", doc.getString("class_name"));
			subScope.append("type",doc.getString("type"));
			subScope.append("isverify", false);
			subScopeComments.insertOne(subScope);
		}
		
	}
	
	public static void main(String[] args) {
		MongoDatabase database = new MongoClient("192.168.1.60", 27017).getDatabase("scopebase");
		
		MongoClientOptions clientOptions = new MongoClientOptions.Builder().connectionsPerHost(50).maxWaitTime(2000)
				.build();
		List<MongoCredential> lstCredentials = Arrays
				.asList(MongoCredential.createMongoCRCredential("liuzhiyong", "admin", "_sysu208".toCharArray()));
		MongoClient client = new MongoClient(new ServerAddress("120.79.66.219",27027), lstCredentials, clientOptions);
		MongoDatabase database2 = client.getDatabase("scopebase");
		MongoCollection<Document> rCommentScopes = database.getCollection("r_comment_scope2");
		MongoCollection<Document> rSubCommentScopes = database.getCollection("r_sub_comment_scope2");
		MongoCollection<Document> rCommentScopes2 = database2.getCollection("r_comment_scope");
		MongoCollection<Document> rSubCommentScopes2 = database2.getCollection("r_sub_comment_scope");
		
		MongoCursor<Document> cursor = rCommentScopes.find().iterator();
		while(cursor.hasNext()) {
			Document doc = cursor.next();
			rCommentScopes2.insertOne(doc);
		}
		
		MongoCursor<Document> cursor2 = rSubCommentScopes.find().iterator();
		while(cursor2.hasNext()) {
			Document doc = cursor2.next();
			rSubCommentScopes2.insertOne(doc);
		}
		
//		generateCommentScopeTable("hadoop");
//		generateCommentScopeTable("hibernate");
//		generateCommentScopeTable("jdk");
//		
//		generateSubCommentScopeTable("hadoop");
//		generateSubCommentScopeTable("hibernate");
//		generateSubCommentScopeTable("jdk");
	
		
	}

}
