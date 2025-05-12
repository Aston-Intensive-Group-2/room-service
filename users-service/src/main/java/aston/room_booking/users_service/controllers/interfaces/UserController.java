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
@RequestMapping("/api/v1/users")
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
     * @param entity модель объекта пользователя
     *
     * @return {@code ResponseEntity<UserDto>} - Ответ, содержащий {@code UserDto}
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
                                    @ExampleObject(name = "userName уже занят", value = ErrorDroConstants.INVALID_USERNAME_ERROR_DTO),
                                    @ExampleObject(name = "email уже занят", value = ErrorDroConstants.INVALID_EMAIL_ERROR_DTO),
                                    @ExampleObject(name = "поле password - пустое", value = ErrorDroConstants.PASSWORD_IS_EMPTY_ERROR_DTO)},
                            mediaType = "application/json")),
            @ApiResponse(
                    responseCode = "500",
                    description = "Внутренняя ошибка при работе сервиса",
                    content = @Content(examples = {@ExampleObject(value = ErrorDroConstants.INTERNAL_SERVER_ERROR_ERROR_DTO)}, mediaType = "application/json"))
    })
    @Operation(summary = "Создать нового пользователя", description = "Создаёт нового пользователя")
    ResponseEntity<?> create(@RequestBody User entity)
            throws DatabaseOperationException,
            ArgumentIsNullException,
            ErrorFetchingUserDataException;

    /**
     * Метод контроллера предоставляет информацию ({@code UserDto}) о текущем пользователе
     * <br/>
     * по информации, извлечённой из {@code SecurityContextHolder}
     * <br/>
     * при успешной аутенфикации
     *
     * @return {@code ResponseEntity<UserDto>} - Ответ, содержащий {@code UserDto}
     *
     * @throws UserNotFoundException Пользователь с указанным {@code id} не найден
     * @throws ErrorFetchingUserDataException Ошибка при парсинге в {@code UserDto}
     */
    @GetMapping
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Пользователь найден",
                    content = @Content(schema = @Schema(implementation = UserDto.class), mediaType = "application/json")),
            @ApiResponse(
                    responseCode = "400",
                    description = "Токен не прошел валидацию",
                    content = @Content(examples = {@ExampleObject(value = ErrorDroConstants.TOKEN_VALIDATION_ERROR_ERROR_DTO)}, mediaType = "application/json")),
            @ApiResponse(
                    responseCode = "401",
                    description = "Пользователь не авторизован",
                    content = @Content(examples = {@ExampleObject(value = ErrorDroConstants.UNAUTHORIZED_ERROR_DTO)}, mediaType = "application/json")),
            @ApiResponse(
                    responseCode = "404",
                    description = "Пользователь не найден",
                    content = @Content(examples = { @ExampleObject(value = ErrorDroConstants.USER_NOT_FOUND_ERROR_DTO)}, mediaType = "application/json")),
            @ApiResponse(
                    responseCode = "500",
                    description = "Внутренняя ошибка при работе сервиса",
                    content = @Content(examples = {@ExampleObject(value = ErrorDroConstants.INTERNAL_SERVER_ERROR_ERROR_DTO )}, mediaType = "application/json"))
    })
    @Operation(summary = "Получить данные текущего авторизованного пользователя", description = "возвращает данные текущего пользователя")
    ResponseEntity<?> get ()
            throws UserNotFoundException,
            ErrorFetchingUserDataException;

    /**
     * Метод контроллера удаляет профиль текущего пользователя
     * <br/>
     * на основании данных, извлечённых из {@code SecurityContextHolder}
     * <br/>
     * при успешной аутенфикации
     *
     * @return {@code ResponseEntity<MessageDto>} - Ответ, содержащий {@code MessageDto} об успешном удалении
     *
     * @throws UserNotFoundException Пользователь с указанным {@code id} не найден
     */
    @DeleteMapping
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Пользователь удалён",
                    content = @Content(examples = {@ExampleObject(value = ErrorDroConstants.USER_DELETED_SUCCESSFUL_MESSAGE_DTO)}, mediaType = "application/json")),
            @ApiResponse(
                    responseCode = "400",
                    description = "Токен не прошел валидацию",
                    content = @Content(examples = {@ExampleObject(value = ErrorDroConstants.TOKEN_VALIDATION_ERROR_ERROR_DTO)}, mediaType = "application/json")),
            @ApiResponse(
                    responseCode = "401",
                    description = "Пользователь не авторизован",
                    content = @Content(examples = {@ExampleObject(value = ErrorDroConstants.UNAUTHORIZED_ERROR_DTO)}, mediaType = "application/json")),
            @ApiResponse(
                    responseCode = "404",
                    description = "Пользователь не найден",
                    content = @Content(examples = { @ExampleObject(value = ErrorDroConstants.USER_NOT_FOUND_ERROR_DTO)}, mediaType = "application/json")),
            @ApiResponse(
                    responseCode = "500",
                    description = "Внутренняя ошибка при работе сервиса",
                    content = @Content(examples = {@ExampleObject(value = ErrorDroConstants.INTERNAL_SERVER_ERROR_ERROR_DTO )}, mediaType = "application/json"))
    })
    @Operation(summary = "Удалить профиль текущего авторизованного пользователя", description = "Удаляет данные текущего пользователя")
    ResponseEntity<?> delete ()
            throws UserNotFoundException;

    /**
     * Метод контроллера обновляет профиль текущего пользователя
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
     * @param entity
     *
     * @return {@code ResponseEntity<MessageDto>}
     *
     * @throws UserNotFoundException Пользователь с указанным {@code id} не найден
     */
    @PutMapping
    @ApiResponses({
            @ApiResponse(
                    responseCode = "202",
                    description = "Профиль пользователя обновлён",
                    content = @Content(schema = @Schema(implementation = UserDto.class), mediaType = "application/json")),
            @ApiResponse(
                    responseCode = "400",
                    description = "Некорректный id или отсутствует тело запроса; либо userName или email уже заняты",
                    content = @Content(schema = @Schema(implementation = ErrorDto.class),
                            examples = {
                                    @ExampleObject(name = "Отсутствует тело запроса", value = ErrorDroConstants.REQUEST_BODY_ERROR_DTO),
                                    @ExampleObject(name = "Токен не прошел валидацию", value = ErrorDroConstants.TOKEN_VALIDATION_ERROR_ERROR_DTO),
                                    @ExampleObject(name = "userName уже занят", value = ErrorDroConstants.INVALID_USERNAME_ERROR_DTO),
                                    @ExampleObject(name = "email уже занят", value = ErrorDroConstants.INVALID_EMAIL_ERROR_DTO),
                                    @ExampleObject(name = "поле password - пустое", value = ErrorDroConstants.PASSWORD_IS_EMPTY_ERROR_DTO)},
                            mediaType = "application/json")),
            @ApiResponse(
                    responseCode = "401",
                    description = "Пользователь не авторизован",
                    content = @Content(examples = {@ExampleObject(value = ErrorDroConstants.UNAUTHORIZED_ERROR_DTO)}, mediaType = "application/json")),
            @ApiResponse(
                    responseCode = "404",
                    description = "Пользователь не найден",
                    content = @Content(examples = { @ExampleObject(value = ErrorDroConstants.USER_NOT_FOUND_ERROR_DTO)}, mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка при работе сервиса",
                    content = @Content(examples = {@ExampleObject(value = ErrorDroConstants.INTERNAL_SERVER_ERROR_ERROR_DTO )}, mediaType = "application/json"))
    })
    @Operation(summary = "Обновить профиль текущего авторизованного пользователя", description = "Обновляет данные текущего пользователя")
    ResponseEntity<?> update (@RequestBody User entity)
            throws ArgumentIsNullException,
            ErrorFetchingUserDataException;

    ResponseEntity<?> changeEmail (@RequestBody String newEmail)
            throws UserNotFoundException,
            ErrorFetchingUserDataException,
            DatabaseOperationException;

    ResponseEntity<?> changePassword (@RequestBody String newPassword)
            throws UserNotFoundException,
            ErrorFetchingUserDataException,
            DatabaseOperationException;
}
