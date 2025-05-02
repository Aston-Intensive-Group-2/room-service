package aston.room_booking.users_service.utils.exceptions;

public class NoUsersFoundException extends RuntimeException {
  public NoUsersFoundException(String message) {
    super(message);
  }
  public NoUsersFoundException(String message, Throwable e) {
    super(message, e);
  }
}
