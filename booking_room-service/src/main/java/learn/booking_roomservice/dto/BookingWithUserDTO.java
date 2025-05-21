package learn.booking_roomservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import learn.booking_roomservice.common.Status;

import java.time.LocalDateTime;
import java.util.UUID;

@Schema(description = "Бронирование с информацией о пользователе")
public record BookingWithUserDTO(
        @Schema(description = "Идентификатор бронирования", example = "f47ac10b-58cc-4372-a567-0e02b2c3d479")
        UUID id,
        @Schema(description = "Информация о пользователе")
        UserDTO user,
        @Schema(description = "Идентификатор помещения", example = "101")
        Long roomId,
        @Schema(description = "Начало бронирования", example = "2025-05-15T13:30:00")
        LocalDateTime start,
        @Schema(description = "Конец бронирования", example = "2025-05-15T15:30:00")
        LocalDateTime end,
        @Schema(description = "Статус бронирования", example = "ACTIVE")
        Status status,
        @Schema(description = "Время создания бронирования", example = "2025-05-15T12:30:00")
        LocalDateTime createdAt
) {
}
