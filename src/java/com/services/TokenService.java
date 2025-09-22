package com.services;

import org.springframework.stereotype.Service;
import java.util.Random;

@Service
public class TokenService {

    private static final int TOKEN_LENGTH = 6; // Length of the verification code
    private static final long EXPIRY_DURATION = 30 * 60 * 1000; // 30 phút (ms)

    // Class chứa email, code và thời gian hết hạn
    public static class TokenInfo {
        private final String email;
        private final long expiryTime;

        public TokenInfo(String email, long expiryTime) {
            this.email = email;
            this.expiryTime = expiryTime;
        }

        public String getEmail() {
            return email;
        }

        public long getExpiryTime() {
            return expiryTime;
        }

        public boolean isExpired() {
            return System.currentTimeMillis() > expiryTime;
        }
    }

    // Generate a random verification code (chỉ nếu bạn cần số)
    public String generateVerificationCode() {
        Random random = new Random();
        StringBuilder code = new StringBuilder();
        for (int i = 0; i < TOKEN_LENGTH; i++) {
            code.append(random.nextInt(10));
        }
        return code.toString();
    }
}
