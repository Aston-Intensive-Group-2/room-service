package aston.room_booking.users_service.utils.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 *
 * @version 1.0
 * @author 4ndr33w
 */
@ResponseStatus(value = HttpStatus.CONFLICT)
public class EmailAlreadyUseException extends RuntimeException {
    public EmailAlreadyUseException(String message) {
        super(message);
    }
    public EmailAlreadyUseException(String message, Throwable e) { super( message, e); }
}
