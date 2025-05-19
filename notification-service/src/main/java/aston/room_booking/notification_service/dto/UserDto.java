package aston.room_booking.notification_service.dto;

public record UserDto(
    Long id,
    String userName,
    String email,
    String firstName,
    String lastName,
    String phone
){

}
