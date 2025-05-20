package aston.room_booking.room_service.controller;

import aston.room_booking.room_service.dto.EquipmentCreateRequestDto;
import aston.room_booking.room_service.dto.EquipmentDto;
import aston.room_booking.room_service.dto.EquipmentUpdateRequestDto;
import aston.room_booking.room_service.service.EquipmentService;
import aston.room_booking.room_service.utilities.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/room-equipments")
@RequiredArgsConstructor
public class EquipmentController {
    private final EquipmentService equipmentService;
    private final TokenService tokenService;

    @GetMapping
    public ResponseEntity<List<EquipmentDto>> getEquipmentList() {
        return ResponseEntity.status(HttpStatus.OK).body(equipmentService.getEquipmentList());
    }

    @GetMapping("/{equipmentId}")
    public ResponseEntity<EquipmentDto> getEquipmentById(@PathVariable Long equipmentId) {
        return ResponseEntity.status(HttpStatus.OK).body(equipmentService.getEquipmentById(equipmentId));
    }

    @PostMapping
    public ResponseEntity<EquipmentDto> createEquipment(@RequestBody EquipmentCreateRequestDto equipmentCreateRequestDto, @RequestHeader("Authorization") String authHeader) {
        if (!tokenService.getUserRoles(authHeader.substring(7)).contains("ADMIN")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return ResponseEntity.status(HttpStatus.OK).body(equipmentService.createEquipment(equipmentCreateRequestDto));
    }

    @PutMapping("/update/{equipmentId}")
    public ResponseEntity<EquipmentDto> updateEquipmentById(@PathVariable Long equipmentId, @RequestBody EquipmentUpdateRequestDto equipmentUpdateRequestDto, @RequestHeader("Authorization") String authHeader) {
        if (!tokenService.getUserRoles(authHeader.substring(7)).contains("ADMIN")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return ResponseEntity.status(HttpStatus.OK).body(equipmentService.updateEquipmentById(equipmentId, equipmentUpdateRequestDto));
    }

    @DeleteMapping("/{equipmentId}")
    public ResponseEntity<Void> deleteEquipmentById(@PathVariable Long equipmentId, @RequestHeader("Authorization") String authHeader) {
        if (!tokenService.getUserRoles(authHeader.substring(7)).contains("ADMIN")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        equipmentService.deleteEquipmentById(equipmentId);
        return ResponseEntity.noContent().build();
    }
}
