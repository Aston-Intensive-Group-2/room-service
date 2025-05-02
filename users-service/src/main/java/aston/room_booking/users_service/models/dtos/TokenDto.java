package aston.room_booking.users_service.models.dtos;

import aston.room_booking.users_service.models.enums.UserRole;

/**
 *
 * @version 1.0
 * @author 4ndr33w
 */
public record TokenDto(
        String email,
        UserRole userRole,
        String token,
        String createdAt) {
}
