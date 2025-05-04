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

    ResponseEntity<?> create(User entity)
            throws EmailAlreadyUseException,
            DatabaseOperationException,
            ErrorFetchingUserDataException;

    ResponseEntity<?> get ()
            throws UserNotFoundException,
            ErrorFetchingUserDataException,
            DatabaseOperationException;

    ResponseEntity<?> delete ()
            throws UserNotFoundException,
            TokenValidationException,
            DatabaseOperationException;

    ResponseEntity<?> update (User user)
            throws UserNotFoundException,
            ErrorFetchingUserDataException,
            DatabaseOperationException;

    ResponseEntity<?> changeEmail (String newEmail)
            throws UserNotFoundException,
            ErrorFetchingUserDataException,
            DatabaseOperationException;

    ResponseEntity<?> changePassword (String newPassword)
            throws UserNotFoundException,
            ErrorFetchingUserDataException,
            DatabaseOperationException;
}
