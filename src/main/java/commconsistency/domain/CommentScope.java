package commconsistency.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection="comment_scope")
public class CommentScope {

	@Id
	private String id;
	
	@Field("comment_id")
	private int commentID;
	
	@Field("class_id")
	private int classID;
	
	private String project;
	
	@Field("class_name")
	private String className;
	
	private String type;
	
	@Field("method_start_line")
	private int methodStartLine;
	
	@Field("method_end_line")
	private int methodEndLine;
	
	@Field("scope_start_line")
	private int scopeStartLine;
	
	@Field("scope_end_line")
	private int scopeEndLine;
	
	@Field("comment_start_line")
	private int commentStartLine;
	
	@Field("comment_end_line")
	private int commentEndLine;
	
	@Field("codes")
	private List<String> codeList;
	
	private List<Integer> verifyScopeEndLineList;
	
	@Field("filter1")
	private boolean filter1=false;
	@Field("filter2")
	private boolean filter2=false;
	
	public int getCommentID() {
		return commentID;
	}
	public void setCommentID(int commentID) {
		this.commentID = commentID;
	}
	public String getProject() {
		return project;
	}
	public void setProject(String project) {
		this.project = project;
	}
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public int getMethodStartLine() {
		return methodStartLine;
	}
	public void setMethodStartLine(int methodStartLine) {
		this.methodStartLine = methodStartLine;
	}
	public int getMethodEndLine() {
		return methodEndLine;
	}
	public void setMethodEndLine(int methodEndLine) {
		this.methodEndLine = methodEndLine;
	}
	public int getScopeStartLine() {
		return scopeStartLine;
	}
	public void setScopeStartLine(int scopeStartLine) {
		this.scopeStartLine = scopeStartLine;
	}
	public int getScopeEndLine() {
		return scopeEndLine;
	}
	public void setScopeEndLine(int scopeEndLine) {
		this.scopeEndLine = scopeEndLine;
	}
	public int getCommentStartLine() {
		return commentStartLine;
	}
	public void setCommentStartLine(int commentStartLine) {
		this.commentStartLine = commentStartLine;
	}
	public int getCommentEndLine() {
		return commentEndLine;
	}
	public void setCommentEndLine(int commentEndLine) {
		this.commentEndLine = commentEndLine;
	}
	public List<String> getCodeList() {
		return codeList;
	}
	public void setCodeList(List<String> codeList) {
		this.codeList = codeList;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public List<Integer> getVerifyScopeEndLineList() {
		return verifyScopeEndLineList;
	}
	public void setVerifyScopeEndLineList(List<Integer> verifyScopeEndLineList) {
		this.verifyScopeEndLineList = verifyScopeEndLineList;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public int getClassID() {
		return classID;
	}
	public void setClassID(int classID) {
		this.classID = classID;
	}
	public boolean isFilter1() {
		return filter1;
	}
	public void setFilter1(boolean filter1) {
		this.filter1 = filter1;
	}
	public boolean isFilter2() {
		return filter2;
	}
	public void setFilter2(boolean filter2) {
		this.filter2 = filter2;
	}
	
	
}