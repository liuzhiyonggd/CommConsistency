package commconsistency.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import commconsistency.dao.MethodExtractorRepository;
import commconsistency.domain.MethodExtractor;

@Service
public class MethodExtractorService {

	@Autowired
	private MethodExtractorRepository methodExtractorRepository;

	public Page<MethodExtractor> findByVerify(boolean verify,Pageable pageable) {
		Page<MethodExtractor> methodExtractorPage = methodExtractorRepository.findByVerify(verify,pageable);
		
		return methodExtractorPage;
	}
	
	public MethodExtractor findByProjectAndCommitIDAndClassName(String project,String commitID,String className) {
		return methodExtractorRepository.findASingleByProjectAndCommitIDAndClassName(project, commitID, className);
	}
	
	public MethodExtractor findByMethodExtractorId(int methodExtractorId) {
		return methodExtractorRepository.findASingleByMethodExtractorId(methodExtractorId);
	}
	
	public void save(MethodExtractor methodExtractor) {
		methodExtractorRepository.save(methodExtractor);
	}
}
