package aston.room_booking.users_service.controllers;

import aston.room_booking.users_service.controllers.interfaces.AdminController;
import aston.room_booking.users_service.models.dtos.ErrorDto;
import aston.room_booking.users_service.models.dtos.UserDto;
import aston.room_booking.users_service.models.entities.User;
import aston.room_booking.users_service.services.interfaces.AdminService;
import aston.room_booking.users_service.utils.ErrorDroConstants;
import aston.room_booking.users_service.utils.StaticConstants;
import aston.room_booking.users_service.utils.exceptions.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Objects;

/**
 * @author 4ndr33w
 * @version 1.0
 */
@Slf4j
@RestController
@AllArgsConstructor
@PreAuthorize("hasAuthority('ADMIN')")
@Tag(name = "Контроллер администрирования пользователей", description = "REST API контроллер администрирования пользовательских данных")
public class AdminsController implements AdminController {

    private final AdminService adminService;

    @Override
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
    public ResponseEntity<?> getAll()
            throws NoUsersFoundException,
            ErrorFetchingUserDataException  {

        Collection<UserDto> users = adminService.getAll();
        return ResponseEntity.status(HttpStatus.OK).body(users);
    }

    @Override
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
    public ResponseEntity<?> getById(@PathVariable long id)
            throws UserNotFoundException,
            InvalidArgumentException,
            ErrorFetchingUserDataException {

        Objects.requireNonNull(id);
        var result = adminService.getById(id);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @Override
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
    public ResponseEntity<?> deletById(@PathVariable long id)
            throws UserNotFoundException,
            DatabaseOperationException,
            InvalidArgumentException{

        var result = adminService.deleteById(id);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @Override
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
    public ResponseEntity<?> updateById(@PathVariable long id, @RequestBody User user)
            throws DatabaseOperationException,
            UserNotFoundException,
            InvalidArgumentException,
            ErrorFetchingUserDataException,
            ArgumentIsNullException {

        var result = adminService.updateById(id, user);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(result);
    }

    @Override
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
    public ResponseEntity<?> create(@Valid @RequestBody User user)
            throws DatabaseOperationException,
            ArgumentIsNullException,
            ErrorFetchingUserDataException{

        var newUser = adminService.create(user);
        if(newUser != null) {
            return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(StaticConstants.UNABLE_TO_CREATE_NEW_USER);
    }
}
