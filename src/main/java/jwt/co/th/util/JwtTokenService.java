package jwt.co.th.util;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import jwt.co.th.model.Role;
import jwt.co.th.model.UserModel;

public class JwtTokenService {
	static final long EXPIRATION_TIME_ACCESS_TOKEN = 1 * 60 * 1000; // 1 min
	static final long EXPIRATION_TIME_REFRESH_TOKEN = 10 * 60 * 1000;
	static final Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());

	public static String getAccess_token(User userCredential, String issuer) {
		List<String> grantedAuthorities = userCredential.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());
		String acess_token = JWT.create().withSubject(userCredential.getUsername())
				.withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME_ACCESS_TOKEN)).withIssuer(issuer)
				.withClaim("role", grantedAuthorities).sign(algorithm);
		return acess_token;
	}

	public static String getAccess_token(UserModel user, String issuer) {
		List<String> grantedAuthorities = user.getRoles().stream().map(Role::getName).collect(Collectors.toList());
		String acess_token = JWT.create().withSubject(user.getEmail())
				.withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME_ACCESS_TOKEN)).withIssuer(issuer)
				.withClaim("role", grantedAuthorities).sign(algorithm);
		return acess_token;
	}
	
	public static String getRefresh_token(User userCredential, String issuer) {
		String refresh_token = JWT.create().withSubject(userCredential.getUsername())
				.withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME_REFRESH_TOKEN)).withIssuer(issuer)
				.sign(algorithm);
		return refresh_token;
	}


}
