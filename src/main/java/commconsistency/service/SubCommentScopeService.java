package commconsistency.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import commconsistency.dao.SubCommentScopeRepository;
import commconsistency.domain.SubCommentScope;

@Service
public class SubCommentScopeService {
	@Autowired
	private SubCommentScopeRepository subCommentScopeRepository;
	
	public SubCommentScope findByCommentID(int commentID) {
		SubCommentScope subComment = subCommentScopeRepository.findByCommentID(commentID);
		return subComment;
	}
	
	public Page<SubCommentScope> findByIsVerify(boolean isVerify,Pageable pageable){
		Page<SubCommentScope> subCommentScopeList = subCommentScopeRepository.findByIsVerify(isVerify, pageable);
		return subCommentScopeList;
	}
	
	public void save(SubCommentScope subCommentScope) {
		subCommentScopeRepository.save(subCommentScope);
	}

}
