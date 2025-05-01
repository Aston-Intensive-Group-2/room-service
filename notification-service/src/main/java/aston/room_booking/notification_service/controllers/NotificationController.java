package aston.room_booking.notification_service.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/notifications")
public class NotificationController {

    @GetMapping("/hello")
    public String hello() {
        return "Hello from Notification Controller!";
    }
}
