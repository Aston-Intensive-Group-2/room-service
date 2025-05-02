package aston.room_booking.users_service.controllers;

import aston.room_booking.users_service.controllers.interfaces.AdminController;
import aston.room_booking.users_service.models.dtos.UserDto;
import aston.room_booking.users_service.models.entities.User;
import aston.room_booking.users_service.services.interfaces.BaseService;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.util.ResourceSet;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.List;

/**
 * @author 4ndr33w
 * @version 1.0
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin")
public class AdminsController implements AdminController {

    private final BaseService userService;

    @GetMapping("/hello")
    public String hello() {
        return "Hello from Admin Controller!";
    }

    @Override
    @GetMapping
    public ResponseEntity<?> getAll() {

        Collection<UserDto> users = userService.getAll();
        return ResponseEntity.status(HttpStatus.OK).body(users);
    }

    @Override
    public boolean changeRole(String newRole, long id) {


        return false;
    }

    @Override
    public ResponseEntity<?>  getById(long id) {
        return null;
    }

    @Override
    public boolean deletById(long id) {
        return false;
    }

    @Override
    public UserDto updateById(long id, UserDto item) {
        return null;
    }

    @Override
    public ResponseEntity<?> create(User entity) {
        return null;
    }
}
