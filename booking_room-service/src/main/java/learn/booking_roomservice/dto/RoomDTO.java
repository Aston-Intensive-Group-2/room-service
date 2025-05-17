package learn.booking_roomservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import learn.booking_roomservice.common.RoomStatus;

@Schema(description = "Информацией о помещении")
public record RoomDTO(
        @Schema(description = "Идентификатор помещения", example = "1")
        Long roomId,
        @Schema(description = "Название помещения", example = "Конференц зал")
        Integer roomWidth,
        @Schema(description = "Ширина помещения", example = "5")
        Integer roomLength,
        @Schema(description = "Длина помещения", example = "10")
        Integer roomHeight,
        @Schema(description = "Высота помещения", example = "3")
        String roomAddress,
        @Schema(description = "Адрес помещения", example = "г. Москва, ул. Ленина, д. 1")
        RoomStatus roomStatus
) {
}
