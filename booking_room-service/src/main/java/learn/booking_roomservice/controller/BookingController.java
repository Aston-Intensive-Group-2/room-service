package learn.booking_roomservice.controller;

import learn.booking_roomservice.common.Status;
import learn.booking_roomservice.model.Booking;
import learn.booking_roomservice.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;

@RestController
@RequestMapping("/booking")
@RequiredArgsConstructor
public class BookingController {
    private final BookingService bookingService;

    @GetMapping("/all")
    public List<Booking> getAll() {
        return bookingService.getAll();
    }

    @PostMapping("/create")
    public Booking createBooking(@RequestBody Booking booking) {
        booking.setStart(LocalDateTime.of(2025, Month.MAY, 15, 13, 30));
        booking.setEnd(LocalDateTime.of(2025, Month.MAY, 15, 15, 30));
        booking.setCreatedAt(LocalDateTime.now());
        booking.setStatus(Status.ACTIVE);
        bookingService.addBooking(booking);
        return booking;
    }
}
