package aston.room_booking.users_service.controllers;

import aston.room_booking.users_service.controllers.interfaces.UserController;
import aston.room_booking.users_service.models.dtos.MessageDto;
import aston.room_booking.users_service.models.entities.User;
import aston.room_booking.users_service.services.interfaces.UserService;
import aston.room_booking.users_service.utils.StaticConstants;
import aston.room_booking.users_service.utils.exceptions.*;
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
    public ResponseEntity<?> delete ()
            throws UserNotFoundException {

        var result = userService.delete();
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @Override
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

    @GetMapping("/api/v1/users/health")
    public ResponseEntity<?> health ()
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
}
