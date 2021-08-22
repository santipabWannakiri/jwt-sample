package jwt.co.th.controller;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import jwt.co.th.model.UserModel;
import jwt.co.th.service.UserService;
import jwt.co.th.util.JwtTokenService;
import jwt.co.th.util.MessageMapping;

@RestController
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserService userService;

	@Autowired
	AuthenticationManager authenticationManager;

	@PostMapping(value = "/register", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String registerUser(@Valid @RequestBody UserModel userInfo) {

		if (userService.findByEmail(userInfo.getEmail()) != null) {
			return MessageMapping.responseCode("6600", "DUPLICATE", "Email already registered");
		}
		if (userService.createUser(userInfo) != null) {
			return MessageMapping.responseCode("0300", "SUCCESS", "Registered successfully");
		} else {
			return MessageMapping.responseCode("0300", "FAILED", "Failed to register");
		}
	}

	@PostMapping("/forgot-password")
	@ResponseBody
	public String forgotPassword(@RequestParam String email) {
		String response = userService.forgotPassword(email);
		if (!response.startsWith("Not")) {
			response = "http://localhost:8080/user/reset-password?token=" + response;
		}
		return response;
	}

	
	@PutMapping("/reset-password")
	public String resetPassword(@RequestParam String token, @RequestParam String password) {
		return userService.resetPassword(token, password);
	}
	

	@GetMapping("/token/refresh")
	@ResponseBody
	public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String authorizationHeader = request.getHeader("Authorization");
		if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
			try {
				String refresh_token = authorizationHeader.substring("Bearer ".length());
				Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
				JWTVerifier verifier = JWT.require(algorithm).build();
				DecodedJWT decodedJWT = verifier.verify(refresh_token);
				String username = decodedJWT.getSubject();
				UserModel user = userService.findByEmail(username);
				if (user == null) {
					throw new Exception("User not found");
				}
				String requester = request.getRequestURI().toString();
				String new_access_token = JwtTokenService.getAccess_token(user, requester);

				Map<String, String> responseMapper = new HashMap<>();
				responseMapper.put("new_access_token", new_access_token);
				responseMapper.put("refresh_token", refresh_token);
				response.setContentType(MediaType.APPLICATION_JSON.toString());
				response.setCharacterEncoding(StandardCharsets.UTF_8.displayName());
				response.getWriter().write(MessageMapping.jwtResponse(new_access_token, refresh_token));
			} catch (Exception e) {
				response.setHeader("ERROR", e.getMessage());
				response.setStatus(400);
				response.setContentType(MediaType.APPLICATION_JSON.toString());
				response.setCharacterEncoding(StandardCharsets.UTF_8.displayName());
				response.getWriter().write(MessageMapping.responseCode("400", "INVALID", e.getMessage()));
			}
		} else {
			response.setHeader("ERROR", "BAD_REQUEST");
			response.setStatus(400);
			response.setContentType(MediaType.APPLICATION_JSON.toString());
			response.setCharacterEncoding(StandardCharsets.UTF_8.displayName());
			response.getWriter().write(MessageMapping.responseCode("400", "BAD_REQUEST", "Refresh token is missing"));
		}
	}

}
