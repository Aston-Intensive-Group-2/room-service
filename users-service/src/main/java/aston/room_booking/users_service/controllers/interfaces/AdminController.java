package aston.room_booking.users_service.controllers.interfaces;

import aston.room_booking.users_service.models.entities.User;
import aston.room_booking.users_service.utils.exceptions.*;
import org.springframework.http.ResponseEntity;

/**
 * Интерфейс контроллера административных операций над профилями пользователей
 *
 * <p>
 *     Предоставляет набор CRUD-операций управления пользователя данными любого пользователя
 *     <br/>
 *     по {@code id} конкретного пользователя
 * </p>
 * <p>
 *     Все операции доступны только при авторизации с ролью администратора: {@code UserRole.ADMIN}
 * </p>
 *
 * @version 1.0
 * @author 4ndr33w
 */
public interface AdminController{

    /**
     * Метод контроллера предоставляет информацию ({@code UserDto}) о пользователе по его {@code id}
     *
     * @param id
     *
     * @return {@code ResponseEntity<UserDto>}
     */
    ResponseEntity<?> getById(long id)
            throws UserNotFoundException,
            ErrorFetchingUserDataException,
            DatabaseOperationException;

    ResponseEntity<?> deletById (long id)
            throws UserNotFoundException,
            ErrorFetchingUserDataException,
            DatabaseOperationException;

    ResponseEntity<?> updateById(long id, User entity)
            throws UserNotFoundException,
            ErrorFetchingUserDataException,
            DatabaseOperationException;

    ResponseEntity<?> getAll()
            throws NoUsersFoundException,
            ErrorFetchingUserDataException,
            DatabaseOperationException;
    
    ResponseEntity<?> create(User entity)
            throws EmailAlreadyUseException,
            DatabaseOperationException,
            ErrorFetchingUserDataException;
}
