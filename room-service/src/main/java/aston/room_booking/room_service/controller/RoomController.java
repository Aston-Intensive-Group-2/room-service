package aston.room_booking.room_service.controller;

import aston.room_booking.room_service.dto.RoomCreateRequestDto;
import aston.room_booking.room_service.dto.RoomDto;
import aston.room_booking.room_service.dto.RoomGetFilteredRequestDto;
import aston.room_booking.room_service.dto.RoomUpdateRequestDto;
import aston.room_booking.room_service.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/rooms")
@RequiredArgsConstructor
public class RoomController {
    private final RoomService roomService;

    @GetMapping
    public ResponseEntity<List<RoomDto>> getRoomList() {
        return ResponseEntity.ok().body(roomService.getRoomList());
    }

    @GetMapping("/filter")
    public ResponseEntity<List<RoomDto>> getFilteredRoomList(@RequestBody RoomGetFilteredRequestDto roomGetFilteredRequestDto) {
        return ResponseEntity.ok().body(roomService.getFilteredRoomList(roomGetFilteredRequestDto));
    }

    @GetMapping("/{roomId}")
    public ResponseEntity<RoomDto> getRoomById(@PathVariable Long roomId) {
        return ResponseEntity.ok().body(roomService.getRoomById(roomId));
    }

    @PostMapping
    public ResponseEntity<RoomDto> createRoom(@RequestBody RoomCreateRequestDto roomCreateRequestDto) {
        return ResponseEntity.ok().body(roomService.createRoom(roomCreateRequestDto));
    }

    @PutMapping
    public ResponseEntity<RoomDto> updateRoom(@RequestBody RoomUpdateRequestDto roomUpdateRequestDto) {
        return ResponseEntity.ok().body(roomService.updateRoom(roomUpdateRequestDto));
    }

    @DeleteMapping("/{roomId}")
    public ResponseEntity<Void> deleteRoomById(@PathVariable Long roomId) {
        roomService.deleteRoomById(roomId);
        return ResponseEntity.noContent().build();
    }
}
