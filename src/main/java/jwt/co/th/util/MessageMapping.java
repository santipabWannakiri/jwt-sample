package jwt.co.th.util;

import org.springframework.stereotype.Service;


@Service
public class MessageMapping {
	
	public static String responseCode(String code, String msgCode, String reason) {
		String response = "{\r\n"
				+ "    \"code\": \""+code+"\",\r\n"
				+ "    \"msgCode\": \""+msgCode+"\",\r\n"
				+ "    \"reason\": \""+reason+"\"\r\n"
				+ "}";
		return response;
	}
	
	public static String jwtResponse(String access_token,String refresh_token) {
		String response = "{\r\n"
				+ "  \"access_token\":\""+access_token+"\",\r\n"
				+ "  \"token_type\":\"bearer\",\r\n"
				+ "  \"expires_in\":3600,\r\n"
				+ "  \"refresh_token\":\""+refresh_token+"\""
				+ "}";
		return response;
	}

}
