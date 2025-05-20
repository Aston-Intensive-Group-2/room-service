package aston.room_booking.room_service.dto;

import aston.room_booking.room_service.model.EquipmentType;

public record EquipmentUpdateRequestDto(
        String equipmentName,
        EquipmentType equipmentType,
        RoomDto roomDto
) {
}
