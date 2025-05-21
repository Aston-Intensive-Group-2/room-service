package learn.booking_roomservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Информация пользователя")
public record UserDTO(
        @Schema(description = "Идентификатор пользователя", example = "101")
        long id,
        @Schema(description = "username пользователя", example = "username")
        String userName,
        @Schema(description = "email пользователя", example = "username@email.com")
        String email,
        @Schema(description = "Имя пользователя", example = "Ivan")
        String firstName,
        @Schema(description = "Фамилия пользователя", example = "Ivanov")
        String lastName,
        @Schema(description = "Номер телефона пользователя", example = "+79123456789")
        String phone
) {
}