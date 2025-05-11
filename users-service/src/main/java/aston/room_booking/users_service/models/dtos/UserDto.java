package aston.room_booking.users_service.models.dtos;

import aston.room_booking.users_service.models.enums.UserRole;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Date;

/**
 *
 * @version 1.0
 * @author 4ndr33w
 */
@Schema(description = "DTO профилья пользователя")
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

    @Schema(description = "Id пользователя", example = "1")
    public long id() {
        return this.id;
    }
    @Schema(description = "UserName пользователя", example = "Andr33w")
    public String userName() {
        return this.userName;
    }
    @Schema(description = "Email пользователя", example = "Andr33w@example.com")
    public String email() {
        return this.email;
    }
    @Schema(description = "FirstName пользователя", example = "Andrew")
    public String firstName() {
        return this.firstName;
    }
    @Schema(description = "LastName пользователя", example = "McFlyev")
    public String lastName() {
        return this.lastName;
    }
    @Schema(description = "Phone number пользователя", example = "+1234567890")
    public String phone() {
        return this.phone;
    }
    @Schema(description = "Роль пользователя", example = "USER", implementation = UserRole.class)
    public UserRole userRole() {
        return this.userRole;
    }
    @Schema(description = "изображение пользователя", example = "null")
    public byte[] image() {
        return this.image;
    }
}