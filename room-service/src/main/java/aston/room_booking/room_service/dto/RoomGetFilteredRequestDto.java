package aston.room_booking.room_service.dto;

import aston.room_booking.room_service.model.RoomStatus;

public record RoomGetFilteredRequestDto(
        Long roomId,
        Integer roomWidth,
        Integer roomLength,
        Integer roomHeight,
        String roomAddress,
        RoomStatus roomStatus,
        RoomEquipmentRequestDto roomEquipmentRequestDto
) {
}
