package commconsistency.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import commconsistency.domain.SubCommentScope;

public interface SubCommentScopeRepository extends MongoRepository<SubCommentScope, String>{
	
	Page<SubCommentScope> findByIsVerify(boolean isVerify,Pageable pageable);
	SubCommentScope findByCommentID(int commentID);

}
