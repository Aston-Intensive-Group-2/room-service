package learn.booking_roomservice.repository;

import learn.booking_roomservice.common.Status;
import learn.booking_roomservice.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface BookingRepository extends JpaRepository<Booking, UUID> {
    /**
     * Возвращает список бронирований в указанном промежутке времени для помещения.
     * @param roomId идентификатор помещения
     * @param start начало бронирования
     * @param end конец бронирования
     * @return список бронирований
     */
    @Query("select b from Booking b where b.roomId = :roomId and b.start < :end and b.end > :start and b.status = 'ACTIVE'")
    List<Booking> getBookingByTime(
            @Param("roomId") Long roomId,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end);

    /**
     * Обновляет статус бронирования.
     * @param oldStatus статус для изменения
     * @param newStatus новый статус
     * @param bookingId идентификатор помещения
     * @param userId идентификатор пользователя
     * @return количество измененных строк
     */
    @Modifying
    @Transactional
    @Query("update Booking b set b.status = :newStatus where b.status = :oldStatus and b.id = :bookingId and b.userId = :userId")
    int updateBookingStatus(
            @Param("oldStatus") Status oldStatus,
            @Param("newStatus") Status newStatus,
            @Param("bookingId") UUID bookingId,
            @Param("userId") Long userId
    );

    /**
     * Получить все бронирования для пользователя.
     * @param userId идентификатор пользователя
     * @return список бронирований
     */
    List<Booking> getAllByUserId(Long userId);
}
