package learn.booking_roomservice.mapper;

import learn.booking_roomservice.dto.BookingDTO;
import learn.booking_roomservice.model.Booking;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BookingMapper {
    BookingDTO toBookingDTO(Booking booking);

    Booking toBooking(BookingDTO bookingDTO);
}
