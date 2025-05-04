package aston.room_booking.users_service.models.dtos;

/**
 *
 * @version 1.0
 * @author 4ndr33w
 */
public record ErrorDto(
        String dateTime,
        int statusCode,
        String message) {
}
