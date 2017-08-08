package commconsistency.domain;

import java.util.Collection;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection="comment_scope")
public class CommentScope {

	@Id
	private String id;
	
	@Field("comment_id")
	private int commentID;
	private String project;
	@Field("commit_id")
	private String commitID;
	@Field("class_name")
	private String className;
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
	private Collection<Line> codeList;
	
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
	public String getCommitID() {
		return commitID;
	}
	public void setCommitID(String commitID) {
		this.commitID = commitID;
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
	public Collection<Line> getCodeList() {
		return codeList;
	}
	public void setCodeList(Collection<Line> codeList) {
		this.codeList = codeList;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	
}
