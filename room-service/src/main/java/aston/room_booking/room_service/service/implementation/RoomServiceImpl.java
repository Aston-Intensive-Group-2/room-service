package aston.room_booking.room_service.service.implementation;

import aston.room_booking.room_service.dto.RoomCreateRequestDto;
import aston.room_booking.room_service.dto.RoomDto;
import aston.room_booking.room_service.dto.RoomGetFilteredRequestDto;
import aston.room_booking.room_service.dto.RoomUpdateRequestDto;
import aston.room_booking.room_service.mapper.EquipmentMapper;
import aston.room_booking.room_service.mapper.RoomMapper;
import aston.room_booking.room_service.model.RoomStatus;
import aston.room_booking.room_service.model.entity.RoomEntity;
import aston.room_booking.room_service.repository.RoomRepository;
import aston.room_booking.room_service.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService {
    private final RoomRepository roomRepository;
    private final RoomMapper roomMapper;
    private final EquipmentMapper equipmentMapper;

    @Override
    public List<RoomDto> getRoomList() {
        return roomMapper.toDto(roomRepository.findAll());
    }

    @Override
    public List<RoomDto> getFilteredRoomList(RoomGetFilteredRequestDto roomGetFilteredRequestDto) {
        // todo
        return null;
    }

    @Override
    public RoomDto getRoomById(Long roomId) {
        return roomMapper.toDto(roomRepository.findById(roomId).get());
    }

    @Override
    public RoomDto createRoom(RoomCreateRequestDto roomCreateRequestDto) {
        RoomEntity futureRoomEntity = RoomEntity.builder()
                .roomWidth(roomCreateRequestDto.roomWidth())
                .roomLength(roomCreateRequestDto.roomLength())
                .roomHeight(roomCreateRequestDto.roomHeight())
                .roomAddress(roomCreateRequestDto.roomAddress())
                .roomStatus(RoomStatus.UNLOCKED)
                .equipmentEntityList(null)
                .build();
        return roomMapper.toDto(roomRepository.save(futureRoomEntity));
    }

    @Override
    public RoomDto updateRoom(RoomUpdateRequestDto roomUpdateRequestDto) {
        RoomEntity currentRoomEntity = roomRepository.findById(roomUpdateRequestDto.roomId()).get();
        currentRoomEntity.setRoomWidth(roomUpdateRequestDto.roomWidth());
        currentRoomEntity.setRoomLength(roomUpdateRequestDto.roomLength());
        currentRoomEntity.setRoomHeight(roomUpdateRequestDto.roomHeight());
        currentRoomEntity.setRoomAddress(roomUpdateRequestDto.roomAddress());
        currentRoomEntity.setRoomStatus(roomUpdateRequestDto.roomStatus());
        currentRoomEntity.setEquipmentEntityList(equipmentMapper.toEntity(roomUpdateRequestDto.equipmentDtoList()));
        return roomMapper.toDto(roomRepository.save(currentRoomEntity));
    }

    @Override
    public void deleteRoomById(Long roomId) {
        roomRepository.deleteById(roomId);
    }
}
