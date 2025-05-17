package aston.room_booking.users_service.models.dtos;

/**
 * @author 4ndr33w
 * @version 1.0
 */
public record LogDto(
        String dateTime,
        long userId,
        String path,
        String message) {
}