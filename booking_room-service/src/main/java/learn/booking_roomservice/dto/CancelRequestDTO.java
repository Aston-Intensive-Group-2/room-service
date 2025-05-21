package learn.booking_roomservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

@Schema(description = "Запрос для идентификатора бронирования")
public record CancelRequestDTO(
        @NotNull(message = "Укажите идентификатор бронирования")
        @Schema(description = "Идентификатор бронирования", example = "f47ac10b-58cc-4372-a567-0e02b2c3d479")
        UUID bookingId
) {
}
