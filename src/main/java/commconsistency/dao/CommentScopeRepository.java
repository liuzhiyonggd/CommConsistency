package commconsistency.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import commconsistency.domain.CommentScope;


public interface CommentScopeRepository extends MongoRepository<CommentScope,String>{

	CommentScope findASingleByCommentID(int commentID);
	
	Page<CommentScope> findByVerifyScopeEndLineList(List<Integer> verifyScopeEndLineList,Pageable pageable);
}
