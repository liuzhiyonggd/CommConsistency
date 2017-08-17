package commconsistency.service;

import org.springframework.beans.factory.annotation.Autowired;
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

}
