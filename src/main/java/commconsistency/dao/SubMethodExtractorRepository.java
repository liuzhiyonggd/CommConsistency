package commconsistency.dao;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import commconsistency.domain.SubMethodExtractor;

public interface SubMethodExtractorRepository extends MongoRepository<SubMethodExtractor, String>{
	
	Page<SubMethodExtractor> findByVerify(boolean verify,Pageable pageable);
	SubMethodExtractor findByMethodExtractorId(int methodExtractorId);

}