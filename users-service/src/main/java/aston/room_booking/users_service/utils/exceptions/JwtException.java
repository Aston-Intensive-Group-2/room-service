package aston.room_booking.users_service.utils.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 *
 * @version 1.0
 * @author 4ndr33w
 */
@ResponseStatus(value = HttpStatus.FAILED_DEPENDENCY)
public class JwtException extends RuntimeException {
  public JwtException(String message) {
    super(message);
  }
  public JwtException(String message, Throwable e) {
    super(message, e);
  }
}
