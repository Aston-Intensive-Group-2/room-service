package aston.room_booking.notification_service.dto;

import aston.room_booking.notification_service.enums.BookingStatus;
import java.time.LocalDateTime;

public record NotificationDto(
    Long userId,
    Long roomId,
    String userName,
    String email,
    String phone,
    LocalDateTime start,
    LocalDateTime end,
    BookingStatus status,
    String message)
{

}
