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
    public ResponseEntity<?> getAll()
            throws NoUsersFoundException,
            ErrorFetchingUserDataException  {

        Collection<UserDto> users = adminService.getAll();
        return ResponseEntity.status(HttpStatus.OK).body(users);
    }

    @Override
    public ResponseEntity<?> getById(@PathVariable long id)
            throws UserNotFoundException,
            InvalidArgumentException,
            ErrorFetchingUserDataException {

        Objects.requireNonNull(id);
        var result = adminService.getById(id);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @Override
    public ResponseEntity<?> deletById(@PathVariable long id)
            throws UserNotFoundException,
            DatabaseOperationException,
            InvalidArgumentException{

        var result = adminService.deleteById(id);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @Override
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
