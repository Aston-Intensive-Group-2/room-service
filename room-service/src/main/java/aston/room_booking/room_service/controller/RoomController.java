package aston.room_booking.room_service.controller;

import aston.room_booking.room_service.dto.*;
import aston.room_booking.room_service.service.RoomService;
import aston.room_booking.room_service.utilities.TokenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/rooms")
@RequiredArgsConstructor
@Tag(name = "Room", description = "API for managing Rooms")
public class RoomController {
    private final RoomService roomService;
    private final TokenService tokenService;

    @GetMapping
    @Operation(summary = "Get list of all rooms")
    public ResponseEntity<List<RoomDto>> getRoomList() {
        return ResponseEntity.status(HttpStatus.OK).body(roomService.getRoomList());
    }

    @GetMapping("/filter")
    @Operation(summary = "Get list of filtered rooms")
    public ResponseEntity<List<RoomDto>> getFilteredRoomList(@RequestBody RoomGetFilteredRequestDto roomGetFilteredRequestDto) {
        return ResponseEntity.status(HttpStatus.OK).body(roomService.getFilteredRoomList(roomGetFilteredRequestDto));
    }

    @GetMapping("/{roomId}")
    @Operation(summary = "Get room by id")
    public ResponseEntity<RoomDto> getRoomById(@PathVariable Long roomId) {
        return ResponseEntity.status(HttpStatus.OK).body(roomService.getRoomById(roomId));
    }

    @PostMapping
    @Operation(summary = "Create some new room")
    public ResponseEntity<RoomDto> createRoom(@RequestBody RoomCreateRequestDto roomCreateRequestDto, @RequestHeader("Authorization") String authHeader) {
        if (!tokenService.getUserRoles(authHeader.substring(7)).contains("ADMIN")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return ResponseEntity.status(HttpStatus.OK).body(roomService.createRoom(roomCreateRequestDto));
    }

    @PutMapping("/update/{roomId}")
    @Operation(summary = "Update room by id")
    public ResponseEntity<RoomDto> updateRoomById(@PathVariable Long roomId, @RequestBody RoomUpdateRequestDto roomUpdateRequestDto, @RequestHeader("Authorization") String authHeader) {
        if (!tokenService.getUserRoles(authHeader.substring(7)).contains("ADMIN")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return ResponseEntity.status(HttpStatus.OK).body(roomService.updateRoomById(roomId, roomUpdateRequestDto));
    }

    @DeleteMapping("/{roomId}")
    @Operation(summary = "Delete room by id")
    public ResponseEntity<Void> deleteRoomById(@PathVariable Long roomId, @RequestHeader("Authorization") String authHeader) {
        if (!tokenService.getUserRoles(authHeader.substring(7)).contains("ADMIN")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        roomService.deleteRoomById(roomId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
