package commconsistency.refactor.rules;

import commconsistency.domain.RefactorType;

public class RefactChange {
	
	private RefactorType type;
	private int newStartLine;
	private int oldStartLine;
	private int newEndLine;
	private int oldEndLine;
	public RefactorType getType() {
		return type;
	}
	public void setType(RefactorType type) {
		this.type = type;
	}
	public int getNewStartLine() {
		return newStartLine;
	}
	public void setNewStartLine(int newStartLine) {
		this.newStartLine = newStartLine;
	}
	public int getOldStartLine() {
		return oldStartLine;
	}
	public void setOldStartLine(int oldStartLine) {
		this.oldStartLine = oldStartLine;
	}
	public int getNewEndLine() {
		return newEndLine;
	}
	public void setNewEndLine(int newEndLine) {
		this.newEndLine = newEndLine;
	}
	public int getOldEndLine() {
		return oldEndLine;
	}
	public void setOldEndLine(int oldEndLine) {
		this.oldEndLine = oldEndLine;
	}
	
	
	
	

}
