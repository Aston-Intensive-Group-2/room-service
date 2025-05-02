package aston.room_booking.users_service.models.dtos;

import aston.room_booking.users_service.models.enums.UserRole;

import java.util.Date;

public record UserDto (
        long id,
        String userName,
        String email,
        String firstName,
        String lastName,
        String phone,
        UserRole userRole,
        byte[] image,
        Date createdAt,
        Date lastLoginDate) {
}