package aston.room_booking.users_service.utils.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 *
 * @version 1.0
 * @author 4ndr33w
 */
@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
public class DatabaseTransactionException extends RuntimeException {
  public DatabaseTransactionException(String message) {
    super(message);
  }
  public DatabaseTransactionException(String message, Throwable e) {
    super(message, e);
  }
}
