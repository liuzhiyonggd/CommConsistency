package commconsistency.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import commconsistency.dao.ConsistencyVerifyRepository;
import commconsistency.dao.EndLineVerifyRepository;
import commconsistency.domain.ConsistencyVerify;
import commconsistency.domain.EndLineVerify;
@Service
public class ConsistencyVerifyService {
	@Autowired
	private ConsistencyVerifyRepository consistencyVerifyRepository;
	
	public void insert(ConsistencyVerify consistencyVerify) {
		consistencyVerifyRepository.insert(consistencyVerify);
	}
	
	public EndLineVerify findByCommentID(int commentID) {
		return consistencyVerifyRepository.findASingleByCommentID(commentID);
	}
	
	public List<ConsistencyVerify> findAll(){
		return consistencyVerifyRepository.findAll();
	}
	
	public Page<ConsistencyVerify> findAll(Pageable pageable){
		return consistencyVerifyRepository.findAll(pageable);
	}
	
	public void save(ConsistencyVerify consistencyVerify) {
		consistencyVerifyRepository.save(consistencyVerify);
	}

}
