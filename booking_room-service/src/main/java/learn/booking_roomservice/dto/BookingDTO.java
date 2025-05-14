package learn.booking_roomservice.dto;

import learn.booking_roomservice.common.Status;

import java.time.LocalDateTime;
import java.util.UUID;

public record BookingDTO(
        UUID id,
        Long userId,
        Long roomId,
        LocalDateTime start,
        LocalDateTime end,
        Status status,
        LocalDateTime createdAt
) {
    public BookingDTO(Long userId, Long roomId, LocalDateTime start, LocalDateTime end) {
        this(null, userId, roomId, start, end, Status.ACTIVE, LocalDateTime.now());
    }
}
