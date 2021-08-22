package jwt.co.th.util;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;

public class ForgotPasswordService {

	private static final long EXPIRE_TOKEN_AFTER_MINUTES = 30;

	public static String generateToken() {
		StringBuilder token = new StringBuilder();
		return token.append(UUID.randomUUID().toString()).append(UUID.randomUUID().toString()).toString();
	}

	public static boolean isTokenExpired(final LocalDateTime tokenCreationDate) {
		LocalDateTime now = LocalDateTime.now();
		Duration diff = Duration.between(tokenCreationDate, now);
		return diff.toMinutes() >= EXPIRE_TOKEN_AFTER_MINUTES;
	}

}
