package aston.room_booking.users_service.controllers;

import aston.room_booking.users_service.services.interfaces.BaseService;
import aston.room_booking.users_service.controllers.interfaces.UserController;
import aston.room_booking.users_service.models.dtos.UserDto;
import aston.room_booking.users_service.models.entities.User;
import aston.room_booking.users_service.utils.StaticConstants;
import aston.room_booking.users_service.utils.exceptions.ArgumentIsNullException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

private final BaseService userService;

    @GetMapping("/hello")
    public String hello() {
        return "Hello from User Controller!";
    }

    @Override
    @PostMapping
    public ResponseEntity<?> create(@RequestBody User user) {
        if(user == null) {
            log.warn(StaticConstants.ARGUMENT_IS_NULL_EXCEPTION_MESSAGE);
            throw new ArgumentIsNullException(StaticConstants.ARGUMENT_IS_NULL_EXCEPTION_MESSAGE);
        }
        var newUser = userService.create(user);
        if(newUser != null) {
            return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(StaticConstants.UNABLE_TO_CREATE_NEW_USER);
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable long id) {
        var result = userService.getById(id);
        if(result != null) {
            return ResponseEntity.status(HttpStatus.OK).body(result);
        }
        return null;
    }

    @Override
    public UserDto getByEmail(String email) {
        return null;
    }

    @Override
    public UserDto getByUserName(String userName) {
        return null;
    }

    @Override
    public boolean changeEmail(String newEmail, long id) {
        return false;
    }

    @Override
    public boolean changePassword(String newPassword, long id) {
        return false;
    }



    @Override
    @DeleteMapping
    public boolean deletById(long id) {
        return false;
    }

    @Override
    public UserDto updateById(long id, UserDto item) {
        return null;
    }


}
