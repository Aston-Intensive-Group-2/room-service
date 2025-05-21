package aston.room_booking.room_service.service.implementation;

import aston.room_booking.room_service.dto.EquipmentCreateRequestDto;
import aston.room_booking.room_service.dto.EquipmentDto;
import aston.room_booking.room_service.dto.EquipmentUpdateRequestDto;
import aston.room_booking.room_service.mapper.EquipmentMapper;
import aston.room_booking.room_service.mapper.RoomMapper;
import aston.room_booking.room_service.model.entity.EquipmentEntity;
import aston.room_booking.room_service.repository.EquipmentRepository;
import aston.room_booking.room_service.service.EquipmentService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EquipmentServiceImpl implements EquipmentService {
    private final EquipmentRepository equipmentRepository;
    private final EquipmentMapper equipmentMapper;
    private final RoomMapper roomMapper;

    @Override
    public List<EquipmentDto> getEquipmentList() {
        return equipmentMapper.toDto(equipmentRepository.findAll());
    }

    @Override
    public EquipmentDto getEquipmentById(Long equipmentId) {
        return equipmentMapper.toDto(equipmentRepository.findById(equipmentId).get());
    }

    @Override
    @Transactional
    public EquipmentDto createEquipment(EquipmentCreateRequestDto equipmentCreateRequestDto) {
        EquipmentEntity futureEquipmentEntity = EquipmentEntity.builder()
                .equipmentName(equipmentCreateRequestDto.equipmentName())
                .equipmentType(equipmentCreateRequestDto.equipmentType())
                .roomEntity(null)
                .build();
        return equipmentMapper.toDto(equipmentRepository.save(futureEquipmentEntity));
    }

    @Override
    @Transactional
    public EquipmentDto updateEquipmentById(Long equipmentId, EquipmentUpdateRequestDto equipmentUpdateRequestDto) {
        EquipmentEntity currentEquipmentEntity = equipmentRepository.findById(equipmentId).get();
        currentEquipmentEntity.setEquipmentName(equipmentUpdateRequestDto.equipmentName());
        currentEquipmentEntity.setEquipmentType(equipmentUpdateRequestDto.equipmentType());
        currentEquipmentEntity.setRoomEntity(roomMapper.toEntity(equipmentUpdateRequestDto.roomDto()));
        return equipmentMapper.toDto(equipmentRepository.save(currentEquipmentEntity));
    }

    @Override
    @Transactional
    public void deleteEquipmentById(Long equipmentId) {
        equipmentRepository.deleteById(equipmentId);
    }
}
