package commconsistency.domain;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection="method_extractor")
public class MethodExtractor {
	@Id
	private String id;
	@Field("method_extractor_id")
	private int methodExtractorId;
	private String project;
	@Field("commit_id")
	private String commitID;
	@Field("class_name")
	private String className;
	@Field("old_start_line")
	private int oldStartLine;
	@Field("old_end_line")
	private int oldEndLine;
	@Field("new_start_line")
	private int newStartLine;
	@Field("new_end_line")
	private int newEndLine;
	@Field("invokes")
	private List<Integer> locations;
	@Field("old_code")
	private List<String> oldCodeList;
	@Field("new_code")
	private List<String> newCodeList;
	@Field("verify")
	private boolean verify;
	@Field("isrefactor")
	private boolean isRefactor;
	
	public boolean isRefactor() {
		return isRefactor;
	}
	public void setRefactor(boolean isRefactor) {
		this.isRefactor = isRefactor;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
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
	public List<String> getOldCodeList() {
		return oldCodeList;
	}
	public void setOldCodeList(List<String> oldCodeList) {
		this.oldCodeList = oldCodeList;
	}
	public List<String> getNewCodeList() {
		return newCodeList;
	}
	public void setNewCodeList(List<String> newCodeList) {
		this.newCodeList = newCodeList;
	}
	public boolean isVerify() {
		return verify;
	}
	public void setVerify(boolean verify) {
		this.verify = verify;
	}
	
	

}
