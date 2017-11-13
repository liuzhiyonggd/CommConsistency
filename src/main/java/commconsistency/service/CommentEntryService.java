package commconsistency.service;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import commconsistency.dao.CommentEntryRepository;
import commconsistency.dao.CommentScopeRepository;
import commconsistency.domain.CommentEntry;
import commconsistency.domain.CommentScope;

@Service
public class CommentEntryService {
	
	@Autowired
	private CommentEntryRepository commentEntryRepository;
	
//	@Cacheable(value="commentScope",key="'commentID_'+#commentID")
	public CommentEntry findByCommentID(int commentID) {
		CommentEntry comment = commentEntryRepository.findASingleByCommentID(commentID);
//		Logger.getLogger(this.getClass()).info("为commentID="+commentID+" 做了缓存.");
		return comment;
	}
	
	
	public void save(CommentEntry comment) {
		commentEntryRepository.save(comment);
	}

}
