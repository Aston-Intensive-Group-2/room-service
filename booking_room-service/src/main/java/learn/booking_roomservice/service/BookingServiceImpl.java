package learn.booking_roomservice.service;

import learn.booking_roomservice.common.Status;
import learn.booking_roomservice.dto.BookingDTO;
import learn.booking_roomservice.exception.BookingNotFoundException;
import learn.booking_roomservice.exception.TimeBookingRegistrationException;
import learn.booking_roomservice.mapper.BookingMapper;
import learn.booking_roomservice.model.Booking;
import learn.booking_roomservice.repository.BookingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final BookingMapper bookingMapper;

    @Override
    public void addBooking(BookingDTO bookingDTO) {
        if (isValidRegistration(bookingDTO)) {
            System.out.println("Valid Registry");
            bookingRepository.save(bookingMapper.toBooking(bookingDTO));
        } else {
            throw new TimeBookingRegistrationException();
        }
    }

    @Override
    public BookingDTO cancelledBooking(UUID bookingId, Long userId) {
        int updated = bookingRepository.updateBookingStatus(Status.ACTIVE, Status.CANCELLED, bookingId, userId);
        if (updated > 0) {
            return bookingMapper.toBookingDTO(
                    bookingRepository.findById(bookingId).orElseThrow(BookingNotFoundException::new));
        } else {
            throw new BookingNotFoundException();
        }
    }

    @Override
    public List<Booking> getAll(Long userId) {
        return bookingRepository.getAllByUserId(userId);
    }

    private boolean isValidRegistration(BookingDTO bookingDTO) {
        List<Booking> listBooking = bookingRepository.getBookingByTime(bookingDTO.roomId(), bookingDTO.start(), bookingDTO.end());
        listBooking.forEach(System.out::println);
        if (listBooking.isEmpty()) {
            return true;
        }
        return false;
    }
}
