package jwt.co.th.serviceImp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import jwt.co.th.model.Role;
import jwt.co.th.model.UserModel;
import jwt.co.th.repository.RoleRepository;
import jwt.co.th.repository.UserRepository;
import jwt.co.th.service.UserService;
import jwt.co.th.util.ForgotPasswordService;

@Service
public class UserServiceImp implements UserService, UserDetailsService {

	@Autowired
	private UserRepository userRepo;

	@Autowired
	private RoleRepository roleRepo;

	@Override
	public UserModel findByEmail(String email) {
		return userRepo.findByEmail(email);
	}
	
	@Override
	public UserModel createUser(UserModel userInfo) {
		userInfo.setPassword(new BCryptPasswordEncoder().encode(userInfo.getPassword()));
		Role userRole = roleRepo.findByName("ROLE_USER");
		userInfo.setRoles(new HashSet<Role>(Arrays.asList(userRole)));
		userInfo.setActive(true);
		return userRepo.save(userInfo);
	}
	
	
	@Override
	public String forgotPassword(String email) {
		UserModel user = userRepo.findByEmail(email);
		if(user == null) {
			return "Not found email";
		}
		user.setToken(ForgotPasswordService.generateToken());
		user.setTokenCreationDate(LocalDateTime.now());
		UserModel userResult = userRepo.save(user);
		return userResult.getToken();
	}

	@Override
	public String resetPassword(String token, String newPassword) {
		UserModel user = userRepo.findByToken(token);
		if(user == null) {
			return "Invalid token";
		}
		if (ForgotPasswordService.isTokenExpired(user.getTokenCreationDate())) {
			return "Token expired.";
		}
		user.setPassword(new BCryptPasswordEncoder().encode(newPassword));
		user.setToken(null);
		user.setTokenCreationDate(null);
		userRepo.save(user);
		return "Your password successfully updated.";
	}
	
	

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		UserModel userInfo = userRepo.findByEmail(email);
		if(userInfo == null) {
			 throw new UsernameNotFoundException("USER NOT FOUND"); // throw back to unsuccessfulAuthentication = Bad credentials(this response undefined that is username or password incorrect because it will get the problem with security) 
		}
		Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
		userInfo.getRoles().forEach(role ->{
			authorities.add(new SimpleGrantedAuthority(role.getName()));
		});

		return org.springframework.security.core.userdetails.User
                .withUsername(userInfo.getEmail())
                .password(userInfo.getPassword())
                .authorities(authorities)
                .accountExpired(false)
                .accountLocked(false)
                .credentialsExpired(false)
                .disabled(false)
                .build();

	}

	
}
