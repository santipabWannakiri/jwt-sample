package jwt.co.th.security;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import jwt.co.th.util.JwtTokenService;
import jwt.co.th.util.MessageMapping;


public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

	private final AuthenticationManager authenticationManager;

	public CustomAuthenticationFilter(AuthenticationManager authenticationManager) {
		this.authenticationManager = authenticationManager;
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {
		String username = request.getParameter("username");
		String password = request.getParameter("password");

		UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username,
				password);
		return authenticationManager.authenticate(authenticationToken);
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {
		User userCredential = (User)authResult.getPrincipal();
		String requester = request.getRequestURI().toString();
		String access_token = JwtTokenService.getAccess_token(userCredential, requester);
		String refresh_token = JwtTokenService.getRefresh_token(userCredential, requester);
		
		Map<String, String> responseMapper = new HashMap<>();
		responseMapper.put("access_token", access_token);
		responseMapper.put("refresh_token", refresh_token);
		response.setContentType(MediaType.APPLICATION_JSON.toString());
		response.setCharacterEncoding(StandardCharsets.UTF_8.displayName());
		response.getWriter().write(MessageMapping.jwtResponse(access_token, refresh_token));
		
	}

}
