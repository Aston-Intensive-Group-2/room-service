package aston.room_booking.users_service.utils;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Component;

/**
 * Утильный класс
 *
 * <ul>
 *   <li>создаёт хэш пароля</li>
 *   <li>производит валидацию пароля по хэшу</li>
 * </ul>
 * @version 1.0
 * @author 4ndr33w
 */
@Component
public class PasswordHash {

    /**
     * Создать хэш пароля
     *
     * @param password чистый пароль
     * @return {@code String} - хэш пароля
     */
    public String createHash(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    /**
     * Производит валидацию пароля и хэша
     *
     * @param rawPassword чистый пароль
     * @param hash хэш пароля
     * @return {@code boolean} - результат валидации: {@code true} в случае успеха, иначе {@code false}
     */
    public boolean checkHash(String rawPassword, String hash) {
        var result = BCrypt.checkpw(rawPassword, hash);
        return result;
    }
}
