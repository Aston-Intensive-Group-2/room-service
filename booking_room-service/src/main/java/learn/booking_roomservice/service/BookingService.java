package learn.booking_roomservice.service;

import learn.booking_roomservice.dto.BookingDTO;
import learn.booking_roomservice.model.Booking;

import java.util.List;
import java.util.UUID;

public interface BookingService {
    void addBooking(BookingDTO bookingDTO);
    BookingDTO cancelledBooking(UUID bookingId, Long userId);
    List<Booking> getAll(Long userId);
}
