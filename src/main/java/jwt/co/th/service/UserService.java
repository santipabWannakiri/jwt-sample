package jwt.co.th.service;

import jwt.co.th.model.UserModel;

public interface UserService {

	public UserModel findByEmail(String email);

	public UserModel createUser(UserModel userInfo);
	
	public String forgotPassword(String email);
	
	public String resetPassword(String token, String password);
	
}
