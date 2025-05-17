package learn.booking_roomservice.service;

import learn.booking_roomservice.dto.BookingDTO;

import java.util.List;
import java.util.UUID;

/**
 * Сервис для управления бронированиями.
 */
public interface BookingService {
    /**
     * Создает новое бронирование.
     * @param bookingDTO данные для создания брони
     * @return созданное бронирование
     * @exception learn.booking_roomservice.exception.TimeBookingRegistrationException если указанное время уже занято
     */
    BookingDTO addBooking(BookingDTO bookingDTO);

    /**
     * Отменяет активное бронирование пользователя.
     * @param bookingId идентификатор бронирования
     * @param userId идентификатор пользователя
     * @return обновленное бронирование с отмененным статусом
     * @exception learn.booking_roomservice.exception.BookingNotFoundException если активное бронирование не найдено
     */
    BookingDTO cancelledBookingByBookingId(UUID bookingId, Long userId);

    /**
     * Возвращает список бронирований пользователя.
     * @param userId идентификатор пользователя
     * @return Список бронирований
     * @exception learn.booking_roomservice.exception.BookingsUserNotFoundException если у пользователя бронирований не найдено
     */
    List<BookingDTO> getAllBookingsByUserId(Long userId);

    /**
     * Возвращает бронирование по идентификатору.
     * @param userId идентификатор пользователя
     * @param bookingId идентификатор бронирования
     * @return бронирование
     * @exception learn.booking_roomservice.exception.BookingNotFoundException если бронирование не найдено
     */
    BookingDTO getBookingById(Long userId, UUID bookingId);
}
