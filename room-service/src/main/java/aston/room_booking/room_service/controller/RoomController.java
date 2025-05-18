package aston.room_booking.room_service.controller;

import aston.room_booking.room_service.dto.*;
import aston.room_booking.room_service.service.RoomService;
import aston.room_booking.room_service.utilities.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/rooms")
@RequiredArgsConstructor
public class RoomController {
    private final RoomService roomService;
    private final TokenService tokenService;

    @GetMapping
    public ResponseEntity<List<RoomDto>> getRoomList() {
        return ResponseEntity.status(HttpStatus.OK).body(roomService.getRoomList());
    }

    @GetMapping("/filter")
    public ResponseEntity<List<RoomDto>> getFilteredRoomList(@RequestBody RoomGetFilteredRequestDto roomGetFilteredRequestDto) {
        return ResponseEntity.status(HttpStatus.OK).body(roomService.getFilteredRoomList(roomGetFilteredRequestDto));
    }

    @GetMapping("/{roomId}")
    public ResponseEntity<RoomDto> getRoomById(@PathVariable Long roomId) {
        return ResponseEntity.status(HttpStatus.OK).body(roomService.getRoomById(roomId));
    }

    @PostMapping
    public ResponseEntity<RoomDto> createRoom(@RequestBody RoomCreateRequestDto roomCreateRequestDto, @RequestHeader("Authorization") String authHeader) {
        if (!tokenService.getUserRoles(authHeader.substring(7)).contains("ADMIN")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return ResponseEntity.status(HttpStatus.OK).body(roomService.createRoom(roomCreateRequestDto));
    }

    @PutMapping
    public ResponseEntity<RoomDto> updateRoom(@RequestBody RoomUpdateRequestDto roomUpdateRequestDto, @RequestHeader("Authorization") String authHeader) {
        if (!tokenService.getUserRoles(authHeader.substring(7)).contains("ADMIN")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return ResponseEntity.status(HttpStatus.OK).body(roomService.updateRoom(roomUpdateRequestDto));
    }

    @DeleteMapping("/{roomId}")
    public ResponseEntity<Void> deleteRoomById(@PathVariable Long roomId, @RequestHeader("Authorization") String authHeader) {
        if (!tokenService.getUserRoles(authHeader.substring(7)).contains("ADMIN")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        roomService.deleteRoomById(roomId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
