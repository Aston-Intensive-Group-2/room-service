package learn.booking_roomservice.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import learn.booking_roomservice.common.Status;

import java.time.LocalDateTime;
import java.util.UUID;

@Schema(description = "Бронирование")
public record BookingDTO(
        @Schema(description = "Идентификатор бронирования", example = "f47ac10b-58cc-4372-a567-0e02b2c3d479")
        UUID id,

        @Schema(description = "Идентификатор пользователя", example = "101")
        Long userId,

        @NotNull(message = "Идентификатор помещения не может быть пустым")
        @Schema(description = "Идентификатор помещения", example = "101")
        Long roomId,

        @Future(message = "Дата начала бронирования должна быть в будущем")
        @NotNull(message = "Начало бронирования должно иметь значение")
        @Schema(description = "Начало бронирования", example = "2025-05-15T13:30:00")
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
        LocalDateTime start,

        @Future(message = "Дата завершения бронирования должна быть в будущем")
        @NotNull(message = "Конец бронирования должен иметь значение")
        @Schema(description = "Конец бронирования", example = "2025-05-15T15:30:00")
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
        LocalDateTime end,

        @Schema(description = "Статус бронирования", example = "ACTIVE")
        Status status,

        @Schema(description = "Время создания бронирования", example = "2025-05-15T12:30:00")
        LocalDateTime createdAt
) {
    public BookingDTO(Long userId, Long roomId, LocalDateTime start, LocalDateTime end) {
        this(null, userId, roomId, start, end, Status.ACTIVE, LocalDateTime.now());
    }
}
