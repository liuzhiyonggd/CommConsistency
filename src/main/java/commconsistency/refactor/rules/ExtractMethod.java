package commconsistency.refactor.rules;

import java.util.List;

import commconsistency.domain.DiffType;
import commconsistency.domain.RefactorType;
import commconsistency.domain.Token;
import commconsistency.refactor.interfaces.Refactor;

public class ExtractMethod extends Refactor{
	
	public ExtractMethod() {
		super();
		setRefactorType(RefactorType.EXTRACTMETHOD);
	}
	@Override
	public boolean isRefactor(List<Token> newTokens, List<Token> oldTokens, List<DiffType> diffs) {
		// TODO Auto-generated method stub
		return false;
	}

}
