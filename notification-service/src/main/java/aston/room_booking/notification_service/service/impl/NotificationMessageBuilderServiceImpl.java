package aston.room_booking.notification_service.service.impl;

import aston.room_booking.notification_service.dto.BookingWithUserDTO;
import aston.room_booking.notification_service.dto.EquipmentDto;
import aston.room_booking.notification_service.dto.RoomDto;
import aston.room_booking.notification_service.service.NotificationMessageBuilderService;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class NotificationMessageBuilderServiceImpl implements NotificationMessageBuilderService {

  private static final DateTimeFormatter DATE_FORMATTER =
      DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

  @Override
  public String buildMessage(RoomDto room, BookingWithUserDTO booking) {
    return switch (booking.status()) {
      case ACTIVE -> buildActiveBookingMessage(room, booking);
      case CLOSED -> buildClosedBookingMessage(room, booking);
      case CANCELLED -> buildCancelledBookingMessage(room, booking);
    };
  }

  private String buildActiveBookingMessage(RoomDto room, BookingWithUserDTO booking) {
    return String.format("""
            ===== Бронирование подтверждено =====
            🔹 Номер брони: %s
            🏢 Помещение: %s (%d кв.м)
            📍 Адрес: %s
            🕒 Время: %s - %s
            🪑 Оборудование: %s
            👤 Ответственный: %s %s (%s)
            📞 Контакты: %s
            """,
        booking.id().toString().substring(0, 8),
        room.roomId(),
        room.roomWidth() * room.roomLength(),
        room.roomAddress(),
        booking.start().format(DATE_FORMATTER),
        booking.end().format(DATE_FORMATTER),
        formatEquipmentList(room.equipmentDtoList()),
        booking.user().firstName(),
        booking.user().lastName(),
        booking.user().userName(),
        booking.user().phone()
    );
  }

  private String buildCancelledBookingMessage(RoomDto room, BookingWithUserDTO booking) {
    return String.format("""
            ===== Бронирование отменено =====
            🔹 Номер брони: %s
            🏢 Помещение: %s
            📅 Дата создания: %s
            🕒 Запланированное время: %s - %s
            👤 Инициатор: %s %s
            ℹ️ Статус помещения: %s
            """,
        booking.id().toString().substring(0, 8),
        room.roomId(),
        booking.createdAt().format(DATE_FORMATTER),
        booking.start().format(DATE_FORMATTER),
        booking.end().format(DATE_FORMATTER),
        booking.user().firstName(),
        booking.user().lastName(),
        room.roomStatus().toString()
    );
  }

  private String buildClosedBookingMessage(RoomDto room, BookingWithUserDTO booking) {
    return String.format("""
            ===== Бронирование завершено =====
            🔹 Номер брони: %s
            🏢 Помещение: %s
            📅 Период использования: %s - %s
            📏 Размеры: %d x %d x %d м
            🪑 Использованное оборудование: %s
            👤 Клиент: %s %s
            🌟 Оставьте отзыв о качестве помещения
            """,
        booking.id().toString().substring(0, 8),
        room.roomId(),
        booking.start().format(DATE_FORMATTER),
        booking.end().format(DATE_FORMATTER),
        room.roomWidth(),
        room.roomLength(),
        room.roomHeight(),
        formatEquipmentList(room.equipmentDtoList()),
        booking.user().firstName(),
        booking.user().lastName()
    );
  }

  private String formatEquipmentList(List<EquipmentDto> equipment) {

    if (equipment.isEmpty()) {
      return "нет";
    }
    return equipment.stream()
        .map(e -> e.equipmentName() + " (" + e.equipmentType() + ")")
        .collect(Collectors.joining(", "));
  }
}
