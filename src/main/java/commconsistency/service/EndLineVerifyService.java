package commconsistency.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import commconsistency.dao.EndLineVerifyRepository;
import commconsistency.domain.EndLineVerify;
@Service
public class EndLineVerifyService {
	@Autowired
	private EndLineVerifyRepository endLineVerifyRepository;
	
	public void insert(EndLineVerify endLineVerify) {
		endLineVerifyRepository.insert(endLineVerify);
	}
	
	public EndLineVerify findByCommentID(int commentID) {
		return endLineVerifyRepository.findASingleByCommentID(commentID);
	}
	
	public Page<EndLineVerify> findAll(Pageable pageable){
		return endLineVerifyRepository.findAll(pageable);
	}

}
