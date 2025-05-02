package aston.room_booking.users_service.utils.exceptions;

public class UserNotFoundException extends RuntimeException {
  public UserNotFoundException(String message) {
    super(message);
  }
  public UserNotFoundException(String message, Throwable e) {
    super(message, e);
  }
}
