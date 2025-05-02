package aston.room_booking.users_service.utils.exceptions;

public class ErrorFetchingUserDataException extends RuntimeException {
    public ErrorFetchingUserDataException(String message) {
        super(message);
    }
    public ErrorFetchingUserDataException(String message, Throwable e) {
        super(message, e);
    }
}
