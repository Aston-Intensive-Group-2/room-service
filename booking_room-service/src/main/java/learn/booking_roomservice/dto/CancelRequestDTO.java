package learn.booking_roomservice.dto;

import java.util.UUID;

public record CancelRequestDTO(
        UUID bookingId
) {
}
