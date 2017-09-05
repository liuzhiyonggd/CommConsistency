package commconsistency.dto;

import java.util.List;


public class MethodExtractorDto {
	private int methodExtractorId;
	private String project;
	private String commitID;
	private String className;
	private int oldStartLine;
	private int oldEndLine;
	private int newStartLine;
	private int newEndLine;
	private List<Integer> locations;
	private String oldCode;
	private String newCode;
	
	public int getMethodExtractorId() {
		return methodExtractorId;
	}
	public void setMethodExtractorId(int methodExtractorId) {
		this.methodExtractorId = methodExtractorId;
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
	public int getOldStartLine() {
		return oldStartLine;
	}
	public void setOldStartLine(int oldStartLine) {
		this.oldStartLine = oldStartLine;
	}
	public int getOldEndLine() {
		return oldEndLine;
	}
	public void setOldEndLine(int oldEndLine) {
		this.oldEndLine = oldEndLine;
	}
	public int getNewStartLine() {
		return newStartLine;
	}
	public void setNewStartLine(int newStartLine) {
		this.newStartLine = newStartLine;
	}
	public int getNewEndLine() {
		return newEndLine;
	}
	public void setNewEndLine(int newEndLine) {
		this.newEndLine = newEndLine;
	}
	public List<Integer> getLocations() {
		return locations;
	}
	public void setLocations(List<Integer> locations) {
		this.locations = locations;
	}
	public String getOldCode() {
		return oldCode;
	}
	public void setOldCode(String oldCode) {
		this.oldCode = oldCode;
	}
	public String getNewCode() {
		return newCode;
	}
	public void setNewCode(String newCode) {
		this.newCode = newCode;
	}
	
	

}
