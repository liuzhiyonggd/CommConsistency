package commconsistency.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import commconsistency.dao.SubCommentEntryRepository;
import commconsistency.domain.SubCommentEntry;

@Service
public class SubCommentEntryService {
	@Autowired
	private SubCommentEntryRepository subCommentEntryRepository;
	
	public SubCommentEntry findByCommentID(int commentID) {
		SubCommentEntry subComment = subCommentEntryRepository.findByCommentID(commentID);
		return subComment;
	}
	
	public Page<SubCommentEntry> findByIsVerify(boolean isVerify,Pageable pageable){
		Page<SubCommentEntry> subCommentEntryList = subCommentEntryRepository.findByIsVerify(isVerify, pageable);
		return subCommentEntryList;
	}
	
	public void save(SubCommentEntry subCommentEntry) {
		subCommentEntryRepository.save(subCommentEntry);
	}

}
