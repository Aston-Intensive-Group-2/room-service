package aston.room_booking.room_service.dto;

import aston.room_booking.room_service.model.EquipmentType;

public record EquipmentCreateRequestDto(
        String equipmentName,
        EquipmentType equipmentType
) {
}
