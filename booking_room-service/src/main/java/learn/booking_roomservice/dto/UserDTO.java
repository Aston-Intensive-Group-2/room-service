package learn.booking_roomservice.dto;

public record UserDTO(
        long id,
        String userName,
        String email,
        String firstName,
        String lastName,
        String phone
) {
}