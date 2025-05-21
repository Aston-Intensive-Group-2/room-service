package aston.room_booking.notification_service.dto;


import aston.room_booking.notification_service.enums.EquipmentType;

public record EquipmentDto(
        Long equipmentId,
        String equipmentName,
        EquipmentType equipmentType,
        RoomDto roomDto
) {
}
