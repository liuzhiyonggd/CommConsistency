package commconsistency.dto;

public class CommentScopeDto {
	
	private int commentID;
	private String project;
	private String commitID;
	private String className;
	private String code;
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
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	
	

}
