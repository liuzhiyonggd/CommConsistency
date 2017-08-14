package commconsistency.dao;

import org.springframework.data.mongodb.repository.MongoRepository;


import commconsistency.domain.User;

public interface UserRepository extends MongoRepository<User,String>{
		User findByUserName(String userName);

}
