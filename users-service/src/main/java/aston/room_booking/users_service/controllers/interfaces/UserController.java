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

    /**
     * Метод контроллера - создаёт пользователя с ролью {@code USER}
     *
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

    /**
     * Метод контроллера предоставляет информацию ({@code UserDto}) о пользователе
     * <br/>
     * по информации, извлечённой из {@code SecurityContextHolder}
     * <br/>
     * при успешной аутенфикации
     *
     * @return {@code ResponseEntity<UserDto>}
     *
     * @throws UserNotFoundException Пользователь с указанным {@code id} не найден
     * @throws ErrorFetchingUserDataException Ошибка при парсинге в {@code UserDto}
     * @throws ArgumentIsNullException Переданный на вход аргумент равен {@code null}
     */
    ResponseEntity<?> get ()
            throws UserNotFoundException,
            ErrorFetchingUserDataException;

    /**
     * Метод контроллера удаляет профиль пользователя
     * <br/>
     * на основании данных, извлечённых из {@code SecurityContextHolder}
     * <br/>
     * при успешной аутенфикации
     *
     * @return {@code ResponseEntity<MessageDto>}
     *
     * @throws UserNotFoundException Пользователь с указанным {@code id} не найден
     */
    ResponseEntity<?> delete ()
            throws UserNotFoundException;

    /**
     * Метод контроллера обновляет профиль пользователя
     * <br/>
     * на основании данных, извлечённых из {@code SecurityContextHolder}
     * <br/>
     * при успешной аутенфикации
     *
     * <hr/>
     * <p>
     *     Поля, допустимые к изменению:
     * <pre>{@code
     * {
     *      "firstName": "Andrew",
     *      "lastName": "McFlyev",
     *      "phone": "1234567890",
     *      "image": null
     * }
     * }</pre>
     * </p>
     *
     * @param user
     *
     * @return {@code ResponseEntity<MessageDto>}
     *
     * @throws UserNotFoundException Пользователь с указанным {@code id} не найден
     */
    ResponseEntity<?> update (User user)
            throws ArgumentIsNullException,
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
