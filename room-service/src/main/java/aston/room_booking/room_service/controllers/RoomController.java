package aston.room_booking.room_service.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @version 1.0
 */
@RestController
@RequestMapping("/api/v1/rooms")
public class RoomController {

    @GetMapping("/hello")
    public String hello() {
        return "Hello from Room-Service Controller!";
    }
}
