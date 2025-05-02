package aston.room_booking.users_service.utils.exceptions;

public class DatabaseTransactionException extends RuntimeException {
  public DatabaseTransactionException(String message) {
    super(message);
  }
  public DatabaseTransactionException(String message, Throwable e) {
    super(message, e);
  }
}
