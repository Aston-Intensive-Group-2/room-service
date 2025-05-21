package aston.room_booking.room_service.dto;

public record RoomCreateRequestDto(
        Integer roomWidth,
        Integer roomLength,
        Integer roomHeight,
        String roomAddress
) {
}
