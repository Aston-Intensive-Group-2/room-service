package aston.room_booking.users_service.models.dtos;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 *
 * @version 1.0
 * @author 4ndr33w
 */
@Schema(description = "DTO сообщения об успешном удалении пользователя")
public record MessageDto(
        String date,
        String message) {

    @Schema(description = "Временная метка события", example = "Fri May 09 00:07:16 GMT+05:00 2025")
    public String date() {
        return this.date;
    }

    @Schema(description = "Содержание сообщения об успешной операции", example = "User is delete successful")
    public String message() {
        return this.message;
    }
}