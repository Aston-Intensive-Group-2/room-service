package aston.room_booking.notification_service.dto;

import aston.room_booking.notification_service.enums.RoomStatus;
import java.util.List;

public record RoomDto(
    Long roomId,
    Integer roomWidth,
    Integer roomLength,
    Integer roomHeight,
    String roomAddress,
    RoomStatus roomStatus,
    List<EquipmentDto> equipmentDtoList
) {
}
