package aston.room_booking.room_service.service;

import aston.room_booking.room_service.dto.EquipmentCreateRequestDto;
import aston.room_booking.room_service.dto.EquipmentDto;
import aston.room_booking.room_service.dto.EquipmentUpdateRequestDto;
import java.util.List;

public interface EquipmentService {
    List<EquipmentDto> getEquipmentList();
    EquipmentDto getEquipmentById(Long equipmentId);
    EquipmentDto createEquipment(EquipmentCreateRequestDto equipmentCreateRequestDto);
    EquipmentDto updateEquipment(EquipmentUpdateRequestDto equipmentUpdateRequestDto);
    void deleteEquipmentById(Long equipmentId);
}
