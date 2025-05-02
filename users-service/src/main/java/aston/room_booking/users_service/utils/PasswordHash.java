package aston.room_booking.users_service.utils;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Configuration
public class PasswordHash {

    public String createHash(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    public boolean checkHash(String rawPassword, String hash) {
        return BCrypt.checkpw(rawPassword, hash);
    }
}
