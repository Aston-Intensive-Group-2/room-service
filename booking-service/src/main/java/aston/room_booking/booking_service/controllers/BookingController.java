package aston.room_booking.booking_service.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/bookings")
public class BookingController {

    @GetMapping("/hello")
    public String hello() {
        return "Hello from Booking Controller!";
    }
}