package aston.room_booking.users_service.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * @author 4ndr33w
 * @version 1.0
 */
@RestController
@RequestMapping("/api/v1/users")
public class UsersController {

    @GetMapping("/hello")
    public String hello() {
        return "Hello from User Controller!";
    }
}
