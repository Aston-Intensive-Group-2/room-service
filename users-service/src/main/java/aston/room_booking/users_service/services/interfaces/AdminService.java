package aston.room_booking.users_service.services.interfaces;

import aston.room_booking.users_service.models.dtos.MessageDto;
import aston.room_booking.users_service.utils.exceptions.*;

import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

/**
 * Интерфейс сервиса администрирования профилей пользователей
 * <p>
 *     все операции доступны только при:
 * <ul>
 *   <li> успешной валидации токена</li>
 *   <li> Установления, что обладатель токена имеет роль {@code ADMIN}</li>
 * </ul>
 * </p>
 * @param <D> DTO пользователя
 * @param <E> Entity, модель объекта
 *
 * @version 1.0
 * @author 4ndr33w
 */
public interface AdminService<D, E> {

    /**
     * Метод создания пользователя с возможностью задать роль
     * <p>
     *     На вход передаётся сущность {@code E entity};
     *     на выходе {@code D dto}
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
     * @param {@code E} entity, сущность
     *
     * @return {@code D} - DTO
     *
     * @throws EmailAlreadyUseException ошибка уникальности email
     * @throws DatabaseOperationException ошибка выполнения операции с базой данных
     * @throws ArgumentIsNullException переданный на вход параметр {@code null}
     * @throws ErrorFetchingUserDataException ошибка парсинга в Dto
     */
    D create(E entity)
            throws EmailAlreadyUseException,
            DatabaseOperationException,
            ArgumentIsNullException,
            ErrorFetchingUserDataException;

    /**
     * Метод возвращает коллекцию всех пользователей
     * <br/>
     * Если текущий пользователь авторизовался с правами администратора
     * <p>
     *     на выходе коллекция преобразуется в DTO: {@code Collection<D>}
     * </p>
     *
     * @return {@code Collection<D>}
     *
     * @throws NoUsersFoundException Не найнено ни одного пользователя или список пуст
     * @throws ErrorFetchingUserDataException ошибка парсинга в Dto
     */
    Collection<D> getAll()
            throws NoUsersFoundException,
            ErrorFetchingUserDataException,
            DatabaseOperationException;

    /**
     * Метод возвращает {@code D} DTO пользователя
     * по переданному в качестве параметра {@code email}
     *
     * @param email email пользователя
     *
     * @return {@code D}
     *
     * @throws UserNotFoundException Пользователь с указанным {@code email} не найден
     * @throws ErrorFetchingUserDataException ошибка парсинга в DTO
     * @throws ArgumentIsNullException Переданный {@code email} равен {@code null}
     */
    D getByEmail(String email)
            throws UserNotFoundException,
            ErrorFetchingUserDataException,
            ArgumentIsNullException,
            DatabaseOperationException;

    /**
     * Метод возвращает {@code D} DTO пользователя
     * по переданному в качестве параметра {@code id}
     *
     * @param id
     *
     * @return {@code D}
     *
     * @throws UserNotFoundException Пользователь с указанным {@code id} не найден
     * @throws ErrorFetchingUserDataException ошибка парсинга в DTO
     */
    D getById( long id)
            throws UserNotFoundException,
            ErrorFetchingUserDataException,
            DatabaseOperationException;

    /**
     * Метод удаляет пользователя
     * по переданному в качестве параметра {@code id}
     *
     * @param id id пользователя
     *
     * @return {@code boolean}
     *
     * @throws UserNotFoundException Пользователь с указанным {@code id} не найден
     */
    MessageDto deleteById(long id)
            throws UserNotFoundException,
            ErrorFetchingUserDataException,
            DatabaseOperationException;

    /**
     * Метод обновления данных профиля пользователя
     * <p>
     *     На вход передаётся {@code D} DTO;
     *     на выходе {@code D dto}
     * </p>
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
     * @param id id пользователя
     * @param {@code D} - DTO пользователя
     *
     * @return {@code D} - DTO
     *
     * @throws UserNotFoundException Пользователь не найден
     * @throws ArgumentIsNullException переданный на вход параметр {@code null}
     * @throws ErrorFetchingUserDataException ошибка парсинга в DTO
     */
    D update(long id, E entity)
            throws UserNotFoundException,
            ErrorFetchingUserDataException,
            ArgumentIsNullException,
            DatabaseOperationException;
}
