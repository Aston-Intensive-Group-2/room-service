package aston.room_booking.notification_service.dto;

import aston.room_booking.notification_service.enums.BookingStatus;
import java.time.LocalDateTime;
import java.util.UUID;

public record BookingWithUserDTO(
    UUID id,
    UserDto user,
    Long roomId,
    LocalDateTime start,
    LocalDateTime end,
    BookingStatus status,
    LocalDateTime createdAt
) {

}

