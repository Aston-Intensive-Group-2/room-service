package learn.booking_roomservice.dto;

import learn.booking_roomservice.common.Status;

import java.time.LocalDateTime;
import java.util.UUID;

public record BookingWithUserDTO(
        UUID id,
        UserDTO user,
        Long roomId,
        LocalDateTime start,
        LocalDateTime end,
        Status status,
        LocalDateTime createdAt
) {
    public BookingWithUserDTO(UUID id, Long roomId, LocalDateTime start, LocalDateTime end, UserDTO user) {
        this(id, user, roomId, start, end, Status.ACTIVE, LocalDateTime.now());
    }
}
