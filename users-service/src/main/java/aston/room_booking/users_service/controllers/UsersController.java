package aston.room_booking.users_service.controllers;

import aston.room_booking.users_service.controllers.interfaces.UserController;
import aston.room_booking.users_service.models.dtos.ErrorDto;
import aston.room_booking.users_service.models.dtos.MessageDto;
import aston.room_booking.users_service.models.dtos.UserDto;
import aston.room_booking.users_service.models.entities.User;
import aston.room_booking.users_service.services.interfaces.UserService;
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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

/**
 * @author 4ndr33w
 * @version 1.0
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name= "Контроллер пользователей", description = "REST API контроллер регистрации и авторизации пользователей")
public class UsersController implements UserController {

private final UserService userService;

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
    public ResponseEntity<?> create (@Valid @RequestBody User user)
            throws DatabaseOperationException,
            ArgumentIsNullException,
            ErrorFetchingUserDataException {

        var newUser = userService.create(user);
        if(newUser != null) {
            return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(StaticConstants.UNABLE_TO_CREATE_NEW_USER);
    }

    @Override
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
    public ResponseEntity<?> get ()
            throws UserNotFoundException,
            ErrorFetchingUserDataException {

        var userDto = userService.get();
        if(userDto != null) {
            return ResponseEntity.status(HttpStatus.OK).body(userDto);
        }
        else {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(
                            new MessageDto(
                                    new Date().toString(),
                                    StaticConstants.USER_NOT_FOUND_EXCEPTION_MESSAGE));
        }
    }

    @Override
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
    public ResponseEntity<?> delete ()
            throws UserNotFoundException {

        var result = userService.delete();
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
    public ResponseEntity<?> update (@RequestBody User user)
            throws ArgumentIsNullException,
            ErrorFetchingUserDataException {

        var result = userService.update(user);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(result);
    }

    @Override
    public ResponseEntity<?> changeEmail(@Valid @RequestBody String newEmail)
            throws UserNotFoundException,
            ErrorFetchingUserDataException,
            DatabaseOperationException {
        return ResponseEntity
                .status(HttpStatus.BANDWIDTH_LIMIT_EXCEEDED)
                .body(
                        new MessageDto(
                                new Date().toString(),
                                "Метод ещё не реализован"));
    }

    @Override
    public ResponseEntity<?> changePassword(@Valid @RequestBody String newPassword)
            throws UserNotFoundException,
            ErrorFetchingUserDataException,
            DatabaseOperationException {
        return ResponseEntity
                .status(HttpStatus.BANDWIDTH_LIMIT_EXCEEDED)
                .body(
                        new MessageDto(
                                new Date().toString(),
                                "Метод ещё не реализован"));
    }
}
