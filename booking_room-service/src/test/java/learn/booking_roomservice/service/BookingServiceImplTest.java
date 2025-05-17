package learn.booking_roomservice.service;

import learn.booking_roomservice.common.Status;
import learn.booking_roomservice.dto.BookingDTO;
import learn.booking_roomservice.exception.BookingNotFoundException;
import learn.booking_roomservice.exception.BookingsUserNotFoundException;
import learn.booking_roomservice.exception.TimeBookingRegistrationException;
import learn.booking_roomservice.mapper.BookingMapper;
import learn.booking_roomservice.model.Booking;
import learn.booking_roomservice.repository.BookingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

/*
Комментарий     | Что обозначает    | Что ты обычно делаешь в этом блоке
given           Исходные данные         Создаёшь DTO, настраиваешь моки
when            Действие                Вызываешь метод, который тестируешь
then            Ожидаемый результат     Проверяешь результат (assert)
verify          Поведение моков         Проверяешь вызов/не вызов методов
 */
class BookingServiceImplTest {

    @Mock
    private BookingRepository bookingRepository;
    @Mock
    private BookingMapper bookingMapper;
    @InjectMocks
    private BookingServiceImpl bookingService;
    private BookingDTO bookingDTO;
    private Booking booking;
    private UUID bookingId;
    private Long userId;

    @BeforeEach
    void setUp() {
        openMocks(this);
        bookingDTO =
                new BookingDTO(1L, 1L,
                        LocalDateTime.of(2025, 5, 15, 13, 30),
                        LocalDateTime.of(2025, 5, 15, 15, 30));
        booking = new Booking();
        bookingId = UUID.randomUUID();
        userId = 2L;
    }

    @Test
    void addBooking_shouldThrowException_whenTimeIsInvalid() {
        // given
        when(bookingRepository.getBookingByTime(bookingDTO.roomId(), bookingDTO.start(), bookingDTO.end()))
                .thenReturn(List.of(new Booking()));

        // when/then
        assertThrows(TimeBookingRegistrationException.class, () -> bookingService.addBooking(bookingDTO));

        // verify
        verify(bookingRepository, times(0)).save(any());
    }

    @Test
    void addBooking_shouldSaveBooking_whenTimeIsValid() {
        // given
        when(bookingRepository.getBookingByTime(bookingDTO.roomId(), bookingDTO.start(), bookingDTO.end()))
                .thenReturn(new ArrayList<>());
        when(bookingMapper.toBooking(bookingDTO)).thenReturn(booking);

        // when/then
        bookingService.addBooking(bookingDTO);

        // verify
        verify(bookingRepository, times(1)).save(booking);
    }

    @Test
    void cancelledBooking_shouldReturnDTO_whenUpdateIsSuccessful() {
        // given
        when(bookingRepository.updateBookingStatus(Status.ACTIVE, Status.CANCELLED, bookingId, userId)).thenReturn(1);
        when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(booking));
        when(bookingMapper.toBookingDTO(booking)).thenReturn(bookingDTO);

        // when
        BookingDTO bookingDTO1 = bookingService.cancelledBookingByBookingId(bookingId, userId);

        // then
        assertEquals(bookingDTO, bookingDTO1);

        // verify
        verify(bookingRepository, (times(1))).updateBookingStatus(Status.ACTIVE, Status.CANCELLED, bookingId, userId);
        verify(bookingRepository, times(1)).findById(bookingId);
    }

    @Test
    void cancelledBooking_shouldThrowException_whenUpdateIsFails() {
        // given
        when(bookingRepository.updateBookingStatus(Status.ACTIVE, Status.CANCELLED, bookingId, userId)).thenReturn(0);

        // when/then
        assertThrows(BookingNotFoundException.class, () -> bookingService.cancelledBookingByBookingId(bookingId, userId));

        // verify
        verify(bookingRepository, times(0)).findById(bookingId);
    }

    @Test
    void getAll_shouldReturnListOfDTOs_whenBookingsExist() {
        // given
        List<Booking> list = List.of(booking);
        List<BookingDTO> listDto = List.of(bookingDTO);
        when(bookingRepository.getAllByUserId(userId)).thenReturn(list);
        when(bookingMapper.toBookingDTO(booking)).thenReturn(bookingDTO);

        // when
        List<BookingDTO> bookingDTOS = bookingService.getAllBookingsByUserId(userId);

        // then
        assertEquals(listDto, bookingDTOS);

        // verify
        verify(bookingRepository, times(1)).getAllByUserId(userId);
        verify(bookingMapper, times(1)).toBookingDTO(booking);
    }

    @Test
    void getAll_shouldReturnException_whenBookingsNotExist() {
        // given
        List<Booking> emptyBookings = new ArrayList<>();
        when(bookingRepository.getAllByUserId(userId)).thenReturn(emptyBookings);

        // when/then
        assertThrows(BookingsUserNotFoundException.class, () -> bookingService.getAllBookingsByUserId(userId));

        // verify
        verify(bookingRepository, times(1)).getAllByUserId(userId);
        verify(bookingMapper, times(0)).toBookingDTO(any());
    }
}
