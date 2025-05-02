package aston.room_booking.users_service.controllers.interfaces;

import aston.room_booking.users_service.models.entities.User;
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
     * @param authorizationHeader
     * @param id
     *
     * @return
     */
    ResponseEntity<?> getById(String authorizationHeader, long id);

    ResponseEntity<?> deletById (String authorizationHeader, long id);
    ResponseEntity<?> updateById(String authorizationHeader, long id, User entity);

    ResponseEntity<?> getAll(String authorizationHeader);
    //ResponseEntity<?> changeRole (String authorizationHeader, String newRole, long id);
    ResponseEntity<?> create(String authorizationHeader, User entity);
}
