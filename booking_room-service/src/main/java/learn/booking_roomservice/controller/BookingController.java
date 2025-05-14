package learn.booking_roomservice.controller;

import learn.booking_roomservice.clients.UserServerProxy;
import learn.booking_roomservice.dto.BookingDTO;
import learn.booking_roomservice.dto.CancelRequestDTO;
import learn.booking_roomservice.dto.UserDTO;
import learn.booking_roomservice.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/booking")
@RequiredArgsConstructor
public class BookingController {
    private final BookingService bookingService;
    private final UserServerProxy userServerProxy;

    @GetMapping("/all")
    public ResponseEntity<List<BookingDTO>> getAll(@RequestHeader("Authorization") String authHeader) {
        UserDTO user = userServerProxy.get(authHeader).getBody();
        List<BookingDTO> bookings = bookingService.getAll(user.id());
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(bookings);
    }

    @PostMapping("/create")
    public ResponseEntity<BookingDTO> createBooking(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody BookingDTO bookingDTO) {
        UserDTO user = userServerProxy.get(authHeader).getBody();
        BookingDTO bookingDTO1 = new BookingDTO(user.id(), bookingDTO.roomId(), bookingDTO.start(), bookingDTO.end());
        bookingService.addBooking(bookingDTO1);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(bookingDTO1);
    }

    @PatchMapping("/cancelled")
    public ResponseEntity<BookingDTO> cancelledBooking(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody CancelRequestDTO cancelRequestDTO
            ) {
        UserDTO user = userServerProxy.get(authHeader).getBody();
        BookingDTO bookingDTO = bookingService.cancelledBooking(cancelRequestDTO.bookingId(), user.id());
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(bookingDTO);
    }
}
