package aston.room_booking.users_service.utils.exceptions;

public class DatabaseOperationException extends RuntimeException {
    public DatabaseOperationException(String message) {
        super(message);
    }
    public DatabaseOperationException(String message, Throwable e) {
        super(message, e);
    }
}
