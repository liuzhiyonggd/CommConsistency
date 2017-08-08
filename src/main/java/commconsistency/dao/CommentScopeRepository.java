package commconsistency.dao;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import commconsistency.domain.CommentScope;


public interface CommentScopeRepository extends MongoRepository<CommentScope,String>{

	CommentScope findByCommentID(int commentID);
}
