package aston.room_booking.users_service.services.interfaces;

import aston.room_booking.users_service.models.dtos.MessageDto;
import aston.room_booking.users_service.utils.exceptions.*;
import jakarta.ws.rs.core.SecurityContext;

/**
 * Интерфейс сервиса CRUD-операций с профилями пользователей
 * <p>
 *     Доступен без проверки ролей по факту успешной аутенфикации.
 *     <br/>
 *     Для успешного выполнения операций из {@link SecurityContext} извлекается {@code id} аутенфицированного пользователя.
 *     <p>
 *    Для всех операций кроме создания пользователя.
 *     </p>

 *     <br/>
 *    Метод {@code create} свободно работает без авторизации.
 * </p>
 * <p>
 *     Единственное условие при создании пользователя - должны быть уникальными поля:
 * <ul>
 *   <li> {@code userName}</li>
 *   <li> {@code email}</li>
 * </ul>
 *     и не пустым поле
 * <ul>
 *   <li> {@code password}</li>
 * </ul>
 * </p>
 * Пользователь может удалять / редактировать / получать только данные своего профиля - полученные по {@code token}
 *
 * @param <D> DTO пользователя
 * @param <E> Entity, модель объекта
 *
 * @version 1.0
 * @author 4ndr33w
 */
public interface UserService<D, E> {

    /**
     * Метод создания пользователя
     *
     * <ul>
     *   <li>На вход передаётся сущность {@code E} Entity;</li>
     *   <li>на выходе {@code D} DTO</li>
     * </ul>
     *
     * <ul>
     *   <li>Вновь созданному пользователю принудительно ставится роль {@code USER};</li>
     *   <li>пароль хэшируется</li>
     * </ul>
     * <hr/>
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
     *      "lastName": "McFlyev",
     *      "phone": "1234567890",
     *      "image": null
     * }
     * }</pre>
     * </p>
     *
     * @param entity сущность
     *
     * @return {@code D} - DTO
     *
     * @throws DatabaseOperationException ошибка выполнения операции с базой данных
     * @throws ArgumentIsNullException переданный на вход параметр {@code null}
     * @throws ErrorFetchingUserDataException ошибка парсинга в Dto
     */
    D create(E entity)
            throws DatabaseOperationException,
            ArgumentIsNullException,
            ErrorFetchingUserDataException;

    /**
     * Метод возвращает {@code D} DTO пользователя
     * по извлечённому из {@code SecurityContext} {@code id} пользователя
     *
     * @return {@code D} DTO объекта
     *
     * @throws UserNotFoundException Пользователь с зашифрованным в токене {@code email} не найден
     * @throws ErrorFetchingUserDataException ошибка парсинга в DTO
     * @throws DatabaseOperationException ошибка при выполнение запроса на получение данных объекта
     */
    D get()
            throws UserNotFoundException,
            ErrorFetchingUserDataException;

    /**
     * Метод удаляет пользователя
     *     <br/>
     * по извлечённому из {@code SecurityContext} {@code id} пользователя
     *
     * @return {@code MessageDto} true в случае успешного удаления
     *
     * @throws UserNotFoundException Пользователь с указанным {@code id} не найден
     */
    MessageDto delete()
            throws UserNotFoundException;

    /**
     * Метод обновления данных профиля пользователя
     * <p>
     *     На вход передаётся {@code E} Entity, содержашая изменённые поля объекта;
     *     <br/>
     * у извлечённого из {@code SecurityContext} {@code User} обновляются поля,
     *     <br/>
     *     затем обновлённый профиль сохраняется в базу данных
     * </p>
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
     * @param entity - обновленные данные в Entity пользователя
     *
     * @return {@code D} - DTO с обновлёнными полями
     *
     * @throws ArgumentIsNullException переданный на вход параметр {@code null}
     * @throws ErrorFetchingUserDataException ошибка парсинга в DTO
     * @throws DatabaseOperationException ошибка при выполнение запроса обновления объекта
     */
    D update(E entity)
            throws ArgumentIsNullException,
            ErrorFetchingUserDataException,
            DatabaseOperationException;
}