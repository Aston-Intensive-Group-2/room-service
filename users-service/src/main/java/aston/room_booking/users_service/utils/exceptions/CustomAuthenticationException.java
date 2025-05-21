package aston.room_booking.users_service.utils.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 *
 * @version 1.0
 * @author 4ndr33w
 */
@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class CustomAuthenticationException extends RuntimeException {
  public CustomAuthenticationException(String message) {
    super(message);
  }
  public CustomAuthenticationException(String message, Throwable e) {
    super(message, e);
  }
}
