package aston.room_booking.users_service.controllers.interfaces;

import aston.room_booking.users_service.models.entities.User;

import aston.room_booking.users_service.utils.exceptions.*;
import org.springframework.http.ResponseEntity;

/**
 * Интерфейс контроллера пользовательских операций
 *
 * <p>
 *     Предоставляет набор CRUD-операций управления пользователя данными своего профиля
 *     по результату успешной аутенфикации
 * </p>
 *
 * @version 1.0
 * @author 4ndr33w
 */
public interface UserController {

    ResponseEntity<?> create(User entity) throws EmailAlreadyUseException, DatabaseOperationException, ArgumentIsNullException, ErrorFetchingUserDataException;
    ResponseEntity<?> get (String authorizationHeader) throws UserNotFoundException, ErrorFetchingUserDataException, TokenValidationException, DatabaseOperationException;
    ResponseEntity<?> delete (String authorizationHeader) throws UserNotFoundException, TokenValidationException, DatabaseOperationException;
    ResponseEntity<?> update (String authorizationHeader, User user) throws UserNotFoundException, TokenValidationException, ArgumentIsNullException, ErrorFetchingUserDataException, DatabaseOperationException;

    ResponseEntity<?> changeEmail (String newEmail);
    ResponseEntity<?> changePassword (String newPassword);
}
