package commconsistency.service;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import commconsistency.dao.CommentScopeRepository;
import commconsistency.domain.CommentScope;

@Service
public class CommentScopeService {
	
	@Autowired
	private CommentScopeRepository commentScopeRepository;
	
	@Cacheable(value="commentScope",key="'commentID_'+#commentID")
	public CommentScope findByCommentID(int commentID) {
		CommentScope comment = commentScopeRepository.findByCommentID(commentID);
		Logger.getLogger(this.getClass()).info("为commentID="+commentID+" 做了缓存.");
		return comment;
	}
	
	public Page<CommentScope> findByVerifyScopeEndLineList(List<Integer> verifyScopeEndLineList,Pageable pageable){
		Page<CommentScope> commentScopeList = commentScopeRepository.findByVerifyScopeEndLineList(verifyScopeEndLineList, pageable);
		return commentScopeList;
	}
	
	public void save(CommentScope commentScope) {
		commentScopeRepository.save(commentScope);
	}

}
