package aston.room_booking.room_service.mapper;

import aston.room_booking.room_service.dto.RoomDto;
import aston.room_booking.room_service.mapper.utility.CycleAvoidingMappingContext;
import aston.room_booking.room_service.model.entity.RoomEntity;
import org.mapstruct.*;
import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        uses = {CycleAvoidingMappingContext.class, EquipmentMapper.class})
public interface RoomMapper {

    @Mapping(target = "equipmentDtoList", source = "equipmentEntityList")
    RoomDto toDto(RoomEntity roomEntity);

    @InheritInverseConfiguration
    RoomEntity toEntity(RoomDto roomDto);

    List<RoomDto> toDto(List<RoomEntity> roomEntityList);

    List<RoomEntity> toEntity(List<RoomDto> roomDtoList);
}
