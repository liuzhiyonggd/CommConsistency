package commconsistency.utils;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.internal.compiler.lookup.Scope;

import com.mongodb.BasicDBObject;
import com.mongodb.Block;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;

public class TableGenerator {

	public static void main2(String[] args) {
		MongoDatabase database = new MongoClient("192.168.1.128", 27017).getDatabase("sourcebase");
		MongoCollection<Document> classes = database.getCollection("class_message2");
		MongoCollection<Document> comments = database.getCollection("comment5");
		MongoCollection<Document> scopeComments = database.getCollection("comment_scope");

		BasicDBObject query = new BasicDBObject();
		query.put("project", "hibernate");

		FindIterable<Document> iter = comments.find(query).limit(200);
		MongoCursor<Document> cursor = iter.iterator();
		while (cursor.hasNext()) {
			Document doc = cursor.next();
			String project = doc.getString("project");
			String commitID = doc.getString("commit_id");
			String className = doc.getString("class_name");
			System.out.println(project+":"+commitID+":"+className);
			int oldStartLine = doc.getInteger("old_scope_startline");
			int oldEndLine = doc.getInteger("old_scope_endline");
			BasicDBObject query2 = new BasicDBObject();
			query2.put("project", project);
			query2.put("commit_id", commitID);
			query2.put("class_name", className);
			Document clazz = classes.find(query2).first();

			List<Document> codes = (List<Document>) clazz.get("old_code");
			StringBuilder sb = new StringBuilder();
			for (Document code : codes) {
				String str = code.getString("line");
				sb.append(str).append("\r\n");
			}

			ASTParser astParser = ASTParser.newParser(AST.JLS3);
			astParser.setSource(sb.toString().toCharArray());
			astParser.setKind(ASTParser.K_COMPILATION_UNIT);

			CompilationUnit unit = (CompilationUnit) (astParser.createAST(null));
			TypeDeclaration type = (TypeDeclaration)unit.types().get(0);
			
			MethodDeclaration[] methods = type.getMethods();
			int methodStartLine = 0,methodEndLine=0;
			for(MethodDeclaration method:methods) {
				int t_methodStartLine = unit.getLineNumber(method.getStartPosition());
				int t_methodEndLine = unit.getLineNumber(method.getStartPosition()+method.getLength()-1);
				
				if(t_methodStartLine<=oldStartLine&&t_methodEndLine>=oldEndLine) {
					methodStartLine = t_methodStartLine;
					methodEndLine = t_methodEndLine;
					break;
				}
			}
			
			Document scopeDocument = new Document();
			scopeDocument.put("comment_id", doc.get("comment_id"));
			scopeDocument.put("project", project);
			scopeDocument.put("commit_id", commitID);
			scopeDocument.put("class_name", className);
			scopeDocument.put("method_start_line", methodStartLine);
			scopeDocument.put("method_end_line", methodEndLine);
			scopeDocument.put("scope_start_line", oldStartLine);
			scopeDocument.put("scope_end_line", oldEndLine);
			scopeDocument.put("comment_start_line", doc.get("old_comment_startline"));
			scopeDocument.put("comment_end_line", doc.get("old_comment_endline"));
			scopeDocument.put("codes", codes);
			
			scopeComments.insertOne(scopeDocument);

		}

	}
	
	public static void main3(String[] args) {
		MongoDatabase database = new MongoClient("192.168.1.128", 27017).getDatabase("sourcebase");
		MongoCollection<Document> scopeComments = database.getCollection("comment_scope");
		
		FindIterable<Document> iter = scopeComments.find();
		MongoCursor<Document> cursor = iter.iterator();
		int id = 1;
		while(cursor.hasNext()) {
			Document doc = cursor.next();
			BasicDBObject query = new BasicDBObject();
			query.put("_id",doc.get("_id"));
			doc.put("comment_id", id);
			scopeComments.replaceOne(query,doc);
			id++;
		}
	}
	
	public static void main(String[] args) {
		MongoDatabase database1 = new MongoClient("192.168.1.128", 27017).getDatabase("sourcebase");
		MongoCollection<Document> scopeComments1 = database1.getCollection("comment_scope");
		
		MongoDatabase database2 = new MongoClient("39.108.99.24", 27017).getDatabase("sourcebase");
		MongoCollection<Document> scopeComments2 = database2.getCollection("comment_scope");
		
		MongoCursor<Document> cursor = scopeComments1.find().iterator();
		while(cursor.hasNext()) {
			Document doc = cursor.next();
			List<Integer> verifyScopeEndLine = new ArrayList<Integer>();
			doc.put("vscope_end_line", verifyScopeEndLine);
			BasicDBObject query = new BasicDBObject();
			query.put("comment_id", doc.get("comment_id"));
			scopeComments1.replaceOne(query, doc);
			scopeComments2.insertOne(doc);
		}
	}

}
