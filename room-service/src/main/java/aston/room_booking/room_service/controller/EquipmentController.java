package aston.room_booking.room_service.controller;

import aston.room_booking.room_service.dto.EquipmentCreateRequestDto;
import aston.room_booking.room_service.dto.EquipmentDto;
import aston.room_booking.room_service.dto.EquipmentUpdateRequestDto;
import aston.room_booking.room_service.service.EquipmentService;
import aston.room_booking.room_service.utilities.TokenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/room-equipments")
@RequiredArgsConstructor
@Tag(name = "Room equipment", description = "API for managing room equipment")
public class EquipmentController {
    private final EquipmentService equipmentService;
    private final TokenService tokenService;

    @GetMapping
    @Operation(summary = "Get list of all room equipments")
    public ResponseEntity<List<EquipmentDto>> getEquipmentList() {
        return ResponseEntity.status(HttpStatus.OK).body(equipmentService.getEquipmentList());
    }

    @GetMapping("/{equipmentId}")
    @Operation(summary = "Get equipment by id")
    public ResponseEntity<EquipmentDto> getEquipmentById(@PathVariable Long equipmentId) {
        return ResponseEntity.status(HttpStatus.OK).body(equipmentService.getEquipmentById(equipmentId));
    }

    @PostMapping
    @Operation(summary = "Create some new equipment")
    public ResponseEntity<EquipmentDto> createEquipment(@RequestBody EquipmentCreateRequestDto equipmentCreateRequestDto, @RequestHeader("Authorization") String authHeader) {
        if (!tokenService.getUserRoles(authHeader.substring(7)).contains("ADMIN")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return ResponseEntity.status(HttpStatus.OK).body(equipmentService.createEquipment(equipmentCreateRequestDto));
    }

    @PutMapping("/update/{equipmentId}")
    @Operation(summary = "Update equipment by id")
    public ResponseEntity<EquipmentDto> updateEquipmentById(@PathVariable Long equipmentId, @RequestBody EquipmentUpdateRequestDto equipmentUpdateRequestDto, @RequestHeader("Authorization") String authHeader) {
        if (!tokenService.getUserRoles(authHeader.substring(7)).contains("ADMIN")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return ResponseEntity.status(HttpStatus.OK).body(equipmentService.updateEquipmentById(equipmentId, equipmentUpdateRequestDto));
    }

    @DeleteMapping("/{equipmentId}")
    @Operation(summary = "Delete equipment by id")
    public ResponseEntity<Void> deleteEquipmentById(@PathVariable Long equipmentId, @RequestHeader("Authorization") String authHeader) {
        if (!tokenService.getUserRoles(authHeader.substring(7)).contains("ADMIN")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        equipmentService.deleteEquipmentById(equipmentId);
        return ResponseEntity.noContent().build();
    }
}
