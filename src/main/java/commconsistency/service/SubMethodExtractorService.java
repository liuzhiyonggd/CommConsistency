package commconsistency.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import commconsistency.dao.SubMethodExtractorRepository;
import commconsistency.domain.SubMethodExtractor;

@Service
public class SubMethodExtractorService {
	@Autowired
	private SubMethodExtractorRepository subMethodExtractorRepository;
	public Page<SubMethodExtractor> findByVerify(boolean verify,Pageable pageable){
		return subMethodExtractorRepository.findByVerify(verify,pageable);
	}
	public SubMethodExtractor findByMethodExtractorId(int methodExtractorId) {
		return subMethodExtractorRepository.findByMethodExtractorId(methodExtractorId);
	}
	public void save(SubMethodExtractor subMethodExtractor) {
		subMethodExtractorRepository.save(subMethodExtractor);
	}

}
