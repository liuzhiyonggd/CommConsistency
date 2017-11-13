package commconsistency.dao;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import commconsistency.domain.SubCommentEntry;

public interface SubCommentEntryRepository extends MongoRepository<SubCommentEntry, String>{
	Page<SubCommentEntry> findByIsVerify(boolean isVerify,Pageable pageable);
	SubCommentEntry findByCommentID(int commentID);
}
