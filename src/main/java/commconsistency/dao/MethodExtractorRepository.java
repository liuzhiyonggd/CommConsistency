package commconsistency.dao;

import java.math.BigInteger;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import commconsistency.domain.MethodExtractor;

public interface MethodExtractorRepository extends MongoRepository<MethodExtractor,String>{
	
	Page<MethodExtractor> findAll(Pageable pageable);
	MethodExtractor findASingleByProjectAndCommitIDAndClassName(String project,String commitID,String className);

	MethodExtractor findASingleByMethodExtractorId(int methodExtractorId);
}
