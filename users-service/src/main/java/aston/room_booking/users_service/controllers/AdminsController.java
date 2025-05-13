package aston.room_booking.users_service.controllers;

import aston.room_booking.users_service.controllers.interfaces.AdminController;
import aston.room_booking.users_service.models.dtos.UserDto;
import aston.room_booking.users_service.models.entities.User;
import aston.room_booking.users_service.services.interfaces.AdminService;
import aston.room_booking.users_service.utils.StaticConstants;

import aston.room_booking.users_service.utils.exceptions.*;
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
@RequestMapping("/api/v1/admin")
public class AdminsController implements AdminController {

    private final AdminService adminService;

    @Override
    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> getAll()
            throws NoUsersFoundException,
            ErrorFetchingUserDataException,
            DatabaseOperationException {

        Collection<UserDto> users = adminService.getAll();
        return ResponseEntity.status(HttpStatus.OK).body(users);
    }

    @Override
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> getById(@PathVariable long id)
            throws UserNotFoundException,
            ErrorFetchingUserDataException,
            DatabaseOperationException {

        Objects.requireNonNull(id);
        var result = adminService.getById(id);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @Override
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> deletById(@PathVariable long id)
            throws UserNotFoundException,
            ErrorFetchingUserDataException,
            DatabaseOperationException {

        var result = adminService.deleteById(id);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @Override
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> updateById(@PathVariable long id, @RequestBody User user)
            throws UserNotFoundException,
            ErrorFetchingUserDataException,
            DatabaseOperationException {

        var result = adminService.update(id, user);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(result);
    }

    @Override
    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> create(@Valid @RequestBody User user)
            throws EmailAlreadyUseException,
            DatabaseOperationException,
            ErrorFetchingUserDataException{

        var newUser = adminService.create(user);
        if(newUser != null) {
            return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(StaticConstants.UNABLE_TO_CREATE_NEW_USER);
    }
}
