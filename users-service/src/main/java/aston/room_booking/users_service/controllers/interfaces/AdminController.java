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
     *
     * @throws UserNotFoundException Пользователь с указанным {@code id} не найден
     * @throws ErrorFetchingUserDataException Ошибка при парсинге в {@code UserDto}
     * @throws ArgumentIsNullException Переданный на вход аргумент равен {@code null}
     */
    ResponseEntity<?> getById(long id)
            throws UserNotFoundException,
            ArgumentIsNullException,
            ErrorFetchingUserDataException;

    /**
     * Метод контроллера - удаляет профиль пользователя по его {@code id}
     *
     * @param id
     *
     * @return {@code ResponseEntity<MessageDto>}
     *
     * @throws UserNotFoundException Пользователь с указанным {@code id} не найден
     * @throws DatabaseOperationException Ошибка при попытке удаления
     * @throws ArgumentIsNullException Переданный на вход аргумент равен {@code null}
     */
    ResponseEntity<?> deletById (long id)
            throws UserNotFoundException,
            ArgumentIsNullException,
            DatabaseOperationException;

    /**
     * Метод контроллера - обновляте профиль пользователя по его {@code id}
     *
     * <p>
     *     Администратор в праве изменить значение любого поля
     *     <br/>
     *     кроме {@code password} и {@code id}
     * <pre>{@code
     * {
     *      "userName": "Andr33w",
     *      "email": "Andr33w@examile.ru",
     *      "firstName": "Andrew",
     *      "userRole": "USER",
     *      "lastName": "McFlyev",
     *      "phone": "1234567890",
     *      "image": null
     * }
     * }</pre>
     * </p>
     *
     * @param id
     * @param entity
     *
     * @return {@code ResponseEntity<UserDto>}
     *
     * @throws ErrorFetchingUserDataException Ошибка при парсинге в {@code UserDto}
     * @throws DatabaseOperationException Ошибка при попытке обновления
     * @throws ArgumentIsNullException Переданный на вход аргумент равен {@code null}
     */
    ResponseEntity<?> updateById(long id, User entity)
            throws DatabaseOperationException,
            ArgumentIsNullException,
            ErrorFetchingUserDataException;

    /**
     * Метод контроллера - возвращает список всех пользователей
     *
     * @return {@code ResponseEntity<Collection<UserDto>>}
     *
     * @throws ErrorFetchingUserDataException Ошибка при парсинге в {@code UserDto}
     * @throws NoUsersFoundException Не найдено ни одного пользователя, либо список пуст
     */
    ResponseEntity<?> getAll()
            throws NoUsersFoundException,
            ErrorFetchingUserDataException;

     /**
     * Метод контроллера - создаёт пользователя с заданной ролью
     * <br/>
     * по умолчанию задаётся роль {@code USER}
     * <p>
     *     На вход передаётся объект {@code User};
     *     на выходе {@code ResponseEntity<UserDto>}
     * </p>
     * <p>
     *     обязательные поля:
     * <pre>{@code
     * {
     *      "userName": "Andr33w",
     *      "email": "Andr33w@examile.ru",
     *      "password": "123qwerty"
     * }
     * }</pre>
     * (поля {@code userName} и {@code email} должны быть уникальными,
     * <br/>
     * {@code password} - не пустым)
     * </p>
     * <p>
     *     Полное заполнение данных:
     * <pre>{@code
     * {
     *      "userName": "Andr33w",
     *      "email": "Andr33w@examile.ru",
     *      "password": "123qwerty",
     *      "firstName": "Andrew",
     *      "userRole": "USER",
     *      "lastName": "McFlyev",
     *      "phone": "1234567890",
     *      "image": null
     * }
     * }</pre>
     * </p>
     *
      * @param entity
      *
     * @return {@code ResponseEntity<UserDto>}
     *
     * @throws ErrorFetchingUserDataException Ошибка при парсинге в {@code UserDto}
      * @throws DatabaseOperationException Ошибка при попытке создания пользователя
      * @throws ArgumentIsNullException Переданный на вход аргумент равен {@code null}
     */
    ResponseEntity<?> create(User entity)
            throws DatabaseOperationException,
            ArgumentIsNullException,
            ErrorFetchingUserDataException;
}
