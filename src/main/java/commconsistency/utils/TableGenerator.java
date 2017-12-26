package commconsistency.utils;


import java.util.ArrayList;
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
	public static void generateCommentScopeTable() {
		MongoDatabase database = new MongoClient("192.168.1.60", 27017).getDatabase("scopebase");
		MongoCollection<Document> classes = database.getCollection("class_message");
		MongoCollection<Document> comments = database.getCollection("comment");
		MongoCollection<Document> scopeComments = database.getCollection("comment_scope");

		BasicDBObject query = new BasicDBObject();
//		query.put("project", "struts");

		FindIterable<Document> iter = comments.find();
		MongoCursor<Document> cursor = iter.iterator();
		while (cursor.hasNext()) {
			Document doc = cursor.next();
//			if(!doc.getString("type").equals("purpose ")) {
//				continue;
//			}
			int classID = doc.getInteger("class_id");
			List<String> commentStrings = (List<String>)doc.get("comment");
			List<String> codeStrings = (List<String>)doc.get("codes");
			
			List<String> words = new ArrayList<String>();
			String splitToken = " .,;:/&|`~%+=-*<>$#@!^\\()[]{}''\"\r\n\t";
			for(String sentense:commentStrings) {
			    StringTokenizer st = new StringTokenizer(sentense,splitToken,false);
			    while(st.hasMoreTokens()) {
			    	words.add(st.nextToken());
			    }
			}
			if(words.size()<5) {
				continue;
			}
			if(codeStrings.size()>30||codeStrings.size()<3) {
				continue;
			}
			
			int startLine = doc.getInteger("scope_start_line");
			int endLine = doc.getInteger("scope_end_line");
			BasicDBObject query2 = new BasicDBObject();
			query2.put("class_id", classID);
			Document clazz = classes.find(query2).first();

			List<String> codes = (List<String>) clazz.get("codes");
			
			StringBuilder sb = new StringBuilder();
			for (String code : codes) {
				sb.append(code).append("\r\n");
			}

			ASTParser astParser = ASTParser.newParser(AST.JLS8);
			astParser.setSource(sb.toString().toCharArray());
			astParser.setKind(ASTParser.K_COMPILATION_UNIT);

			CompilationUnit unit = (CompilationUnit) (astParser.createAST(null));
			TypeDeclaration type = (TypeDeclaration)unit.types().get(0);
			
			MethodDeclaration[] methods = type.getMethods();
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
	
	public static void generateSubCommentScopeTable() {
		MongoDatabase database = new MongoClient("192.168.1.60", 27017).getDatabase("scopebase");
		MongoCollection<Document> scopeComments = database.getCollection("comment_scope");
		MongoCollection<Document> subScopeComments = database.getCollection("sub_comment_scope");
		FindIterable<Document> iter = scopeComments.find();
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
		MongoDatabase database2 = new MongoClient("120.79.66.219", 27027).getDatabase("scopebase");
		MongoCollection<Document> rCommentScopes = database.getCollection("r_comment_scope");
		MongoCollection<Document> rSubCommentScopes = database.getCollection("r_sub_comment_scope");
		MongoCollection<Document> rEndLines = database.getCollection("r_endline_verify");
		MongoCollection<Document> rCommentScopes2 = database2.getCollection("r_comment_scope");
		MongoCollection<Document> rSubCommentScopes2 = database2.getCollection("r_sub_comment_scope");
		MongoCollection<Document> rEndLines2 = database2.getCollection("r_endline_verify");
		MongoCollection<Document> users = database.getCollection("user");
		MongoCollection<Document> users2 = database2.getCollection("user");
		
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
		
		MongoCursor<Document> cursor3 = rEndLines.find().iterator();
		while(cursor3.hasNext()) {
			Document doc = cursor3.next();
			rEndLines2.insertOne(doc);
		}
		
		MongoCursor<Document> cursor4 = users.find().iterator();
		while(cursor4.hasNext()) {
			Document doc = cursor4.next();
			users2.insertOne(doc);
		}
		
		
		
		
	}

}
