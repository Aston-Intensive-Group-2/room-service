package learn.booking_roomservice.service;

import learn.booking_roomservice.dto.BookingDTO;

import java.util.List;
import java.util.UUID;

public interface BookingService {
    BookingDTO addBooking(BookingDTO bookingDTO);

    BookingDTO cancelledBooking(UUID bookingId, Long userId);

    List<BookingDTO> getAll(Long userId);
}
