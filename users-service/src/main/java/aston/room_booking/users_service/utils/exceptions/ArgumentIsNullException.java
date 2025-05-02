package aston.room_booking.users_service.utils.exceptions;

public class ArgumentIsNullException extends RuntimeException {
    public ArgumentIsNullException(String message) {
        super(message);
    }
    public ArgumentIsNullException(String message, Throwable e) {
        super(message, e);
    }
}
