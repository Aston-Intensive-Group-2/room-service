package aston.room_booking.room_service.dto;

import aston.room_booking.room_service.model.EquipmentType;

public record EquipmentDto(
        Long equipmentId,
        String equipmentName,
        EquipmentType equipmentType,
        RoomDto roomDto
) {
}
