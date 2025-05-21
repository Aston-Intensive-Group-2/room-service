package aston.room_booking.notification_service.service;

import aston.room_booking.notification_service.dto.BookingWithUserDTO;
import aston.room_booking.notification_service.dto.RoomDto;

public interface NotificationMessageBuilderService {

  String buildMessage(RoomDto room, BookingWithUserDTO booking);

}
