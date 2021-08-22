package jwt.co.th.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import jwt.co.th.model.UserModel;

@Repository("UserRepository")
public interface UserRepository extends CrudRepository<UserModel, Long>{

	UserModel findByEmail(String email);
	
	UserModel findByToken(String token);
	
}
