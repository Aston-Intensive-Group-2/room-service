package aston.room_booking.users_service.utils.exceptions;

public class EmailAlreadyUseException extends RuntimeException {
    public EmailAlreadyUseException(String message) {
        super(message);
    }
    public EmailAlreadyUseException(String message, Throwable e) { super( message, e); }
}
