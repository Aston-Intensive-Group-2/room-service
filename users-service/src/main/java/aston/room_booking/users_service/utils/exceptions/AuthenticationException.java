package aston.room_booking.users_service.utils.exceptions;

/**
 * @author 4ndr33w
 * @version 1.0
 */
public class AuthenticationException extends RuntimeException {
    public AuthenticationException(String message) {
        super(message);
    }
    public AuthenticationException(String message, Throwable e) {
        super(message, e);
    }
}
