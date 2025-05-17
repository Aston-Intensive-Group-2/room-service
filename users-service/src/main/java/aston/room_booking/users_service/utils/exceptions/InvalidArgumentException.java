package aston.room_booking.users_service.utils.exceptions;

/**
*
* @version 1.0
* @author 4ndr33w
*/
public class InvalidArgumentException extends RuntimeException {
  public InvalidArgumentException(String message) {
    super(message);
  }
  public InvalidArgumentException(String message, Throwable e) {
    super(message, e);
  }
}
