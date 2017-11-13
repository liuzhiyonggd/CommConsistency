package commconsistency.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import commconsistency.domain.CommentEntry;


public interface CommentEntryRepository extends MongoRepository<CommentEntry,String>{

	CommentEntry findASingleByCommentID(int commentID);
	
}
