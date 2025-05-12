package aston.room_booking.users_service.controllers;

import aston.room_booking.users_service.services.interfaces.AdminService;
import aston.room_booking.users_service.controllers.interfaces.UserController;
import aston.room_booking.users_service.models.entities.User;
import aston.room_booking.users_service.services.interfaces.UserService;
import aston.room_booking.users_service.utils.StaticConstants;
import aston.room_booking.users_service.utils.exceptions.*;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
//import org.springframework.validation.annotation.Validated;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

/**
 * @author 4ndr33w
 * @version 1.0
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UsersController implements UserController {

private final UserService userService;

    @Override
    @PostMapping
    public ResponseEntity<?> create (@Valid @RequestBody User user)
            throws EmailAlreadyUseException,
            DatabaseOperationException,
            ErrorFetchingUserDataException {

        var newUser = userService.create(user);
        if(newUser != null) {
            return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(StaticConstants.UNABLE_TO_CREATE_NEW_USER);
    }

    @Override
    @GetMapping
    public ResponseEntity<?> get ()
            throws UserNotFoundException,
            ErrorFetchingUserDataException,
            DatabaseOperationException {

        var userDto = userService.get();
        if(userDto != null) {
            return ResponseEntity.status(HttpStatus.OK).body(userDto);
        }
        else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(false);
        }
    }

    @Override
    @DeleteMapping
    public ResponseEntity<?> delete ()
            throws UserNotFoundException,
            TokenValidationException,
            DatabaseOperationException {

        var result = userService.delete();
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @Override
    @PutMapping
    public ResponseEntity<?> update (@RequestBody User user)
            throws UserNotFoundException,
            ErrorFetchingUserDataException,
            DatabaseOperationException {

        var result = userService.update(user);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(result);
    }


    @Override
    public ResponseEntity<?> changeEmail(@RequestBody String newEmail)
            throws UserNotFoundException,
            ErrorFetchingUserDataException,
            DatabaseOperationException {
        return ResponseEntity.status(HttpStatus.BANDWIDTH_LIMIT_EXCEEDED).body(false);
    }

    @Override
    public ResponseEntity<?> changePassword(@RequestBody String newPassword)
            throws UserNotFoundException,
            ErrorFetchingUserDataException,
            DatabaseOperationException {
        return ResponseEntity.status(HttpStatus.BANDWIDTH_LIMIT_EXCEEDED).body(false);
    }
}
