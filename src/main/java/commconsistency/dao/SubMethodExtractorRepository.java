package commconsistency.dao;

import java.math.BigInteger;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import commconsistency.domain.SubMethodExtractor;

public interface SubMethodExtractorRepository extends MongoRepository<SubMethodExtractor, String>{
	
	Page<SubMethodExtractor> findAll(Pageable pageable);

}