package commconsistency.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import commconsistency.domain.EndLineVerify;

public interface EndLineVerifyRepository extends MongoRepository<EndLineVerify,String>{
	
	List<EndLineVerify> findByUserName(String userName);
	EndLineVerify findASingleByCommentID(int commentID);

}
