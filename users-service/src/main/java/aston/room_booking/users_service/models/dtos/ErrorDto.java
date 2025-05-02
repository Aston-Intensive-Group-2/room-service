package aston.room_booking.users_service.models.dtos;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;

public record ErrorDto(
        OffsetDateTime dateTime,
        int statusCode,
        String path,
        String message) {
}
