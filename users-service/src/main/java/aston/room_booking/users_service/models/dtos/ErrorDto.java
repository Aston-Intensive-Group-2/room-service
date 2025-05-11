package aston.room_booking.users_service.models.dtos;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 *
 * @version 1.0
 * @author 4ndr33w
 */
@Schema(description = "DTO сообщения об ошибке")
public record ErrorDto(
        String dateTime,
        int statusCode,
        String message) {

    @Schema(description = "Временная метка события", example = "Fri May 09 00:07:16 GMT+05:00 2025")
    public String dateTime() {
        return this.dateTime;
    }

    @Schema(description = "Код ответа сервера", example = "500")
    public int statusCode() {
        return this.statusCode;
    }

    @Schema(description = "Содержание сообщения об ошибке", example = "Internal Server Error")
    public String message() {
        return this.message;
    }
}
