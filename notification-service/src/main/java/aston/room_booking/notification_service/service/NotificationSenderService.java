package aston.room_booking.notification_service.service;

import aston.room_booking.notification_service.dto.NotificationDto;
import aston.room_booking.notification_service.enums.NotificationType;

public interface NotificationSenderService {

  void sendNotification(NotificationDto notification);

  NotificationType getNotificationType();
}
