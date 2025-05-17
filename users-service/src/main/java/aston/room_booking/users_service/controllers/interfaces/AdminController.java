package aston.room_booking.users_service.controllers.interfaces;

import aston.room_booking.users_service.models.dtos.ErrorDto;
import aston.room_booking.users_service.models.dtos.UserDto;
import aston.room_booking.users_service.models.entities.User;
import aston.room_booking.users_service.utils.ErrorDroConstants;
import aston.room_booking.users_service.utils.exceptions.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
@RequestMapping("/api/v1/admin")
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
     * @throws InvalidArgumentException Переданный на вход {@code id} - отрицательный
     *
     * @see aston.room_booking.users_service.models.dtos.UserDto
     */
    @GetMapping("/{id}")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Пользователь найден",
                    content = @Content(schema = @Schema(implementation = UserDto.class), mediaType = "application/json")
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Некорректный id",
                    content = @Content(examples = {@ExampleObject(value = ErrorDroConstants.INVALID_ID_ERROR_DTO)}, mediaType = "application/json")),
            @ApiResponse(
                    responseCode = "401",
                    description = "Пользователь не авторизован",
                    content = @Content(examples = {@ExampleObject(value = ErrorDroConstants.UNAUTHORIZED_ERROR_DTO)}, mediaType = "application/json")),
            @ApiResponse(
                    responseCode = "403",
                    description = "У пользователя нет прав доступа",
                    content = @Content(examples = { @ExampleObject(value = ErrorDroConstants.FORBIDDEN_ERROR_DTO)}, mediaType = "application/json")),
            @ApiResponse(
                    responseCode = "404",
                    description = "Пользователь не найден",
                    content = @Content(examples = { @ExampleObject(value = ErrorDroConstants.USER_NOT_FOUND_ERROR_DTO)}, mediaType = "application/json")),
            @ApiResponse(
                    responseCode = "500",
                    description = "Внутренняя ошибка при работе сервиса",
                    content = @Content(examples = {@ExampleObject(value = ErrorDroConstants.INTERNAL_SERVER_ERROR_ERROR_DTO )}, mediaType = "application/json"))
    })
    @Operation(summary = "Получить пользователя по id", description = "возвращает пользователя по id")
    ResponseEntity<?> getById(long id)
            throws UserNotFoundException,
            InvalidArgumentException,
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
     * @throws InvalidArgumentException Переданный на вход {@code id} - отрицательный
     */
    @DeleteMapping("/{id}")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Пользователь удалён",
                    content = @Content(examples = {@ExampleObject(value = ErrorDroConstants.USER_DELETED_SUCCESSFUL_MESSAGE_DTO)}, mediaType = "application/json")),
            @ApiResponse(
                    responseCode = "400",
                    description = "Некорректный id",
                    content = @Content(
                            schema = @Schema(implementation = ErrorDto.class),
                            examples = {
                                    @ExampleObject(name = "Некорректный id", value = ErrorDroConstants.INVALID_ID_ERROR_DTO),
                                    @ExampleObject(name = "Токен не прошел валидацию", value = ErrorDroConstants.TOKEN_VALIDATION_ERROR_ERROR_DTO), },
                            mediaType = "application/json"
                    )),
            @ApiResponse(
                    responseCode = "401",
                    description = "Пользователь не авторизован",
                    content = @Content(examples = {@ExampleObject(value = ErrorDroConstants.UNAUTHORIZED_ERROR_DTO)}, mediaType = "application/json")),
            @ApiResponse(
                    responseCode = "403",
                    description = "У пользователя нет прав доступа",
                    content = @Content(examples = { @ExampleObject(value = ErrorDroConstants.FORBIDDEN_ERROR_DTO)}, mediaType = "application/json")),
            @ApiResponse(
                    responseCode = "404",
                    description = "Пользователь не найден",
                    content = @Content(examples = { @ExampleObject(value = ErrorDroConstants.USER_NOT_FOUND_ERROR_DTO)}, mediaType = "application/json")),
            @ApiResponse(
                    responseCode = "500",
                    description = "Внутренняя ошибка при работе сервиса",
                    content = @Content(examples = {@ExampleObject(value = ErrorDroConstants.INTERNAL_SERVER_ERROR_ERROR_DTO )}, mediaType = "application/json"))
    })
    @Operation(summary = "Удалить пользователя по id", description = "Удаляет пользователя по id")
    ResponseEntity<?> deletById (long id)
            throws UserNotFoundException,
            InvalidArgumentException,
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
     * @throws UserNotFoundException Пользователь не найден
     * @throws DatabaseOperationException Ошибка при попытке обновления
     * @throws InvalidArgumentException Переданный на вход {@code id} - отрицательный
     * @throws ArgumentIsNullException Переданный {@code entity} равен {@code null}
     */
    @PutMapping("/{id}")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "202",
                    description = "Профиль пользователя обновлён",
                    content = @Content(schema = @Schema(implementation = UserDto.class), mediaType = "application/json")),
            @ApiResponse(
                    responseCode = "400",
                    description = "Некорректный id или отсутствует тело запроса; либо userName или email уже заняты",
                    content = @Content(
                            schema = @Schema(implementation = ErrorDto.class),
                            examples = {
                                    @ExampleObject(name = "Некорректный id", value = ErrorDroConstants.INVALID_ID_ERROR_DTO),
                                    @ExampleObject(name = "Отсутствует тело запроса", value = ErrorDroConstants.REQUEST_BODY_ERROR_DTO),
                                    @ExampleObject(name = "Токен не прошел валидацию", value = ErrorDroConstants.TOKEN_VALIDATION_ERROR_ERROR_DTO),
                                    @ExampleObject(name = "userName уже занят", value = ErrorDroConstants.INVALID_USERNAME_ERROR_DTO),
                                    @ExampleObject(name = "email уже занят", value = ErrorDroConstants.INVALID_EMAIL_ERROR_DTO) },
                            mediaType = "application/json"
                    )),
            @ApiResponse(
                    responseCode = "401",
                    description = "Пользователь не авторизован",
                    content = @Content(examples = {@ExampleObject(value = ErrorDroConstants.UNAUTHORIZED_ERROR_DTO)}, mediaType = "application/json")),
            @ApiResponse(
                    responseCode = "403",
                    description = "У пользователя нет прав доступа",
                    content = @Content(examples = { @ExampleObject(value = ErrorDroConstants.FORBIDDEN_ERROR_DTO)}, mediaType = "application/json")),
            @ApiResponse(
                    responseCode = "404",
                    description = "Пользователь не найден",
                    content = @Content(examples = { @ExampleObject(value = ErrorDroConstants.USER_NOT_FOUND_ERROR_DTO)}, mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка при работе сервиса",
                    content = @Content(examples = {@ExampleObject(value = ErrorDroConstants.INTERNAL_SERVER_ERROR_ERROR_DTO )}, mediaType = "application/json"))
    })
    @Operation(summary = "Обновить данные пользователя по id", description = "Обновляет данные пользователя по id")
    ResponseEntity<?> updateById(long id, User entity)
            throws DatabaseOperationException,
            UserNotFoundException,
            InvalidArgumentException,
            ErrorFetchingUserDataException,
            ArgumentIsNullException;

    /**
     * Метод контроллера - возвращает список всех пользователей
     *
     * @return {@code ResponseEntity<Collection<UserDto>>}
     *
     * @throws ErrorFetchingUserDataException Ошибка при парсинге в {@code UserDto}
     * @throws NoUsersFoundException Не найдено ни одного пользователя, либо список пуст
     */
    @GetMapping
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Список пользователей получен",
                    content = @Content(schema = @Schema(implementation = UserDto.class), mediaType = "application/json")),
            @ApiResponse(
                    responseCode = "204",
                    description = "Список пользователей пуст",
                    content = @Content(examples = { @ExampleObject(value = ErrorDroConstants.NO_USERS_FOUND_ERROR_DTO)}, mediaType = "application/json")),
            @ApiResponse(
                    responseCode = "400",
                    description = "Токен не прошел валидацию",
                    content = @Content(examples = {@ExampleObject(value = ErrorDroConstants.TOKEN_VALIDATION_ERROR_ERROR_DTO)}, mediaType = "application/json")
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Пользователь не авторизован",
                    content = @Content(examples = {@ExampleObject(value = ErrorDroConstants.UNAUTHORIZED_ERROR_DTO)}, mediaType = "application/json")),

            @ApiResponse(
                    responseCode = "403",
                    description = "У пользователя нет прав доступа",
                    content = @Content(examples = { @ExampleObject(value = ErrorDroConstants.FORBIDDEN_ERROR_DTO)}, mediaType = "application/json")),
            @ApiResponse(
                    responseCode = "500",
                    description = "Внутренняя ошибка при работе сервиса",
                    content = @Content(examples = {@ExampleObject(value = ErrorDroConstants.INTERNAL_SERVER_ERROR_ERROR_DTO )}, mediaType = "application/json"))
    })
    @Operation(summary = "Получить список всех пользователей", description = "возвращает список всех пользователей")
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
     @PostMapping
     @ApiResponses({
             @ApiResponse(
                     responseCode = "201",
                     description = "Пользователь создан",
                     content = @Content(schema = @Schema(implementation = UserDto.class), mediaType = "application/json")),
             @ApiResponse(
                     responseCode = "400",
                     description = "отсутствует тело запроса, либо userName или email уже заняты",
                     content = @Content(schema = @Schema(implementation = ErrorDto.class),
                             examples = {
                                     @ExampleObject(name = "Отсутствует тело запроса", value = ErrorDroConstants.REQUEST_BODY_ERROR_DTO),
                                     @ExampleObject(name = "Токен не прошел валидацию", value = ErrorDroConstants.TOKEN_VALIDATION_ERROR_ERROR_DTO),
                                     @ExampleObject(name = "userName уже занят", value = ErrorDroConstants.INVALID_USERNAME_ERROR_DTO),
                                     @ExampleObject(name = "email уже занят", value = ErrorDroConstants.INVALID_EMAIL_ERROR_DTO),
                                     @ExampleObject(name = "поле password - пустое", value = ErrorDroConstants.PASSWORD_IS_EMPTY_ERROR_DTO),
                             }, mediaType = "application/json")),
             @ApiResponse(
                     responseCode = "401",
                     description = "Пользователь не авторизован",
                     content = @Content(examples = {@ExampleObject(value = ErrorDroConstants.FORBIDDEN_ERROR_DTO)}, mediaType = "application/json")),
             @ApiResponse(
                     responseCode = "403",
                     description = "У пользователя нет прав доступа",
                     content = @Content(examples = { @ExampleObject(value = ErrorDroConstants.USER_NOT_FOUND_ERROR_DTO)}, mediaType = "application/json")),
             @ApiResponse(
                     responseCode = "500",
                     description = "Внутренняя ошибка при работе сервиса",
                     content = @Content(examples = {@ExampleObject(value = ErrorDroConstants.INTERNAL_SERVER_ERROR_ERROR_DTO)}, mediaType = "application/json"))
     })
     @Operation(summary = "Создать нового пользователя", description = "Создаёт нового пользователя")
    ResponseEntity<?> create(User entity)
            throws DatabaseOperationException,
            ArgumentIsNullException,
            ErrorFetchingUserDataException;
}
