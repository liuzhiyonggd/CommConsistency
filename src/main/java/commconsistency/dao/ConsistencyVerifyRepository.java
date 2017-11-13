package commconsistency.dao;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import commconsistency.domain.ConsistencyVerify;
import commconsistency.domain.EndLineVerify;

public interface ConsistencyVerifyRepository extends MongoRepository<ConsistencyVerify,String>{
	
	List<EndLineVerify> findByUserName(String userName);
	EndLineVerify findASingleByCommentID(int commentID);

}
