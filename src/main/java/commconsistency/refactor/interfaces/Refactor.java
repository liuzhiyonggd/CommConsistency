package commconsistency.refactor.interfaces;

import java.util.List;

import commconsistency.domain.DiffType;
import commconsistency.domain.RefactorType;
import commconsistency.domain.Token;

public abstract class Refactor {
	private RefactorType refactorType;
	public abstract boolean isRefactor(List<Token> newTokens,List<Token> oldTokens,List<DiffType> diffs);
	
	public RefactorType getRefactorType() {
		return refactorType;
	}
	
	public void setRefactorType(RefactorType refactorType) {
		this.refactorType = refactorType;
	}

	
}
	