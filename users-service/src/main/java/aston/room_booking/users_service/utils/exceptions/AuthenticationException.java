package aston.room_booking.users_service.utils.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author 4ndr33w
 * @version 1.0
 */
@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class AuthenticationException extends RuntimeException {
    public AuthenticationException(String message) {
        super(message);
    }
    public AuthenticationException(String message, Throwable e) {
        super(message, e);
    }
}
