package commconsistency.domain;


import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection="submethod_extractor")
public class SubMethodExtractor {
	@Id
	private String id;
	@Field("method_extractor_id")
	private int methodExtractorId;
	private String project;
	@Field("commit_id")
	private String commitID;
	private String commitID2;
	@Field("class_name")
	private String className;
	private String className2;
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
	public String getCommitID2() {
		return commitID2;
	}
	public void setCommitID2(String commitID2) {
		this.commitID2 = commitID2;
	}
	public String getClassName2() {
		return className2;
	}
	public void setClassName2(String className2) {
		this.className2 = className2;
	}
	public boolean isVerify() {
		return verify;
	}
	public void setVerify(boolean verify) {
		this.verify = verify;
	}
	
	

}
