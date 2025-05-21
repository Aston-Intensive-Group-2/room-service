package aston.room_booking.room_service.dto;

public record RoomEquipmentRequestDto(
        Boolean isPrinterIncluded,
        Boolean isScannerIncluded,
        Boolean isConditionerIncluded,
        Boolean isProjectorIncluded
) {
}
