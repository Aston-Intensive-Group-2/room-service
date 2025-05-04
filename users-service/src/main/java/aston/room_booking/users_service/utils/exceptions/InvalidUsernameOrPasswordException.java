package aston.room_booking.users_service.utils.exceptions;

/**
 * @author 4ndr33w
 * @version 1.0
 */
public class InvalidUsernameOrPasswordException extends RuntimeException {
    public InvalidUsernameOrPasswordException(String message) {
        super(message);
    }
    public InvalidUsernameOrPasswordException(String message, Throwable e) {
        super(message, e);
    }
}
