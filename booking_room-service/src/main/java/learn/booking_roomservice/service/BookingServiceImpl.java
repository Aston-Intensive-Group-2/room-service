package learn.booking_roomservice.service;

import learn.booking_roomservice.common.Status;
import learn.booking_roomservice.dto.BookingDTO;
import learn.booking_roomservice.exception.BookingNotFoundException;
import learn.booking_roomservice.exception.BookingsUserNotFoundException;
import learn.booking_roomservice.exception.TimeBookingRegistrationException;
import learn.booking_roomservice.mapper.BookingMapper;
import learn.booking_roomservice.repository.BookingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final BookingMapper bookingMapper;

    @Override
    public BookingDTO addBooking(BookingDTO bookingDTO) {
        if (isValidRegistration(bookingDTO)) {
            bookingRepository.save(bookingMapper.toBooking(bookingDTO));
        } else {
            throw new TimeBookingRegistrationException();
        }
        return bookingDTO;
    }

    @Override
    public BookingDTO cancelledBookingByBookingId(UUID bookingId, Long userId) {
        int updated = bookingRepository.updateBookingStatus(Status.ACTIVE, Status.CANCELLED, bookingId, userId);
        if (updated > 0) {
            return bookingMapper.toBookingDTO(
                    bookingRepository.findById(bookingId).orElseThrow(BookingNotFoundException::new));
        } else {
            throw new BookingNotFoundException();
        }
    }

    @Override
    public List<BookingDTO> getAllBookingsByUserId(Long userId) {
        return Optional.of(bookingRepository.getAllByUserId(userId))
                .filter(list -> !list.isEmpty())
                .orElseThrow(BookingsUserNotFoundException::new)
                .stream()
                .map(bookingMapper::toBookingDTO)
                .toList();
    }

    @Override
    public BookingDTO getBookingById(Long userId, UUID bookingId) {
        return bookingRepository.findById(bookingId)
                .filter(booking -> booking.getUserId().equals(userId))
                .map(bookingMapper::toBookingDTO)
                .orElseThrow(BookingNotFoundException::new);
    }

    private boolean isValidRegistration(BookingDTO bookingDTO) {
        return bookingRepository.getBookingByTime(bookingDTO.roomId(), bookingDTO.start(), bookingDTO.end()).isEmpty();
    }
}