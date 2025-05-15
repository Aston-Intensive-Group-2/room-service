package aston.room_booking.room_service.service;

import aston.room_booking.room_service.dto.RoomCreateRequestDto;
import aston.room_booking.room_service.dto.RoomDto;
import aston.room_booking.room_service.dto.RoomGetFilteredRequestDto;
import aston.room_booking.room_service.dto.RoomUpdateRequestDto;
import java.util.List;

public interface RoomService {
    List<RoomDto> getRoomList();
    List<RoomDto> getFilteredRoomList(RoomGetFilteredRequestDto roomGetFilteredRequestDto);
    RoomDto getRoomById(Long roomId);
    RoomDto createRoom(RoomCreateRequestDto roomCreateRequestDto);
    RoomDto updateRoom(RoomUpdateRequestDto roomUpdateRequestDto);
    void deleteRoomById(Long roomId);
}
