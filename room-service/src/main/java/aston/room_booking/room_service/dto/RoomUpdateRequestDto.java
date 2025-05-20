package aston.room_booking.room_service.dto;

import aston.room_booking.room_service.model.RoomStatus;
import java.util.List;

public record RoomUpdateRequestDto(
        Integer roomWidth,
        Integer roomLength,
        Integer roomHeight,
        String roomAddress,
        RoomStatus roomStatus,
        List<EquipmentDto> equipmentDtoList
) {
}
