package commconsistency.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection="sub_comment_entry6")
public class SubCommentEntry {
	
	@Id
	private String id;
	
	@Field("comment_id")
	private int commentID;
	
	private String project;
	
	@Field("class_name")
	private String className;
	
	private String type;
	
	@Field("isverify")
	private boolean isVerify;
	
	@Field("filter1")
	private boolean filter1;
	
	@Field("filter2")
	private boolean filter2;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
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
	public boolean isVerify() {
		return isVerify;
	}
	public void setVerify(boolean isVerify) {
		this.isVerify = isVerify;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
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
