package aston.room_booking.room_service.mapper;

import aston.room_booking.room_service.dto.EquipmentDto;
import aston.room_booking.room_service.mapper.utility.CycleAvoidingMappingContext;
import aston.room_booking.room_service.model.entity.EquipmentEntity;
import org.mapstruct.*;
import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        uses = {CycleAvoidingMappingContext.class})
public interface EquipmentMapper {

    @Mapping(target = "roomDto", source = "roomEntity", ignore = true)
    EquipmentDto toDto(EquipmentEntity equipmentEntity);

    @InheritInverseConfiguration
    EquipmentEntity toEntity(EquipmentDto equipmentDto);

    List<EquipmentDto> toDto(List<EquipmentEntity> equipmentEntityList);

    List<EquipmentEntity> toEntity(List<EquipmentDto> equipmentDtoList);
}
