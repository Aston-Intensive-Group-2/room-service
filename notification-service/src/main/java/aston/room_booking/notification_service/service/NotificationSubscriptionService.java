package aston.room_booking.notification_service.service;

import aston.room_booking.notification_service.dto.NotificationSubscriptionDto;
import aston.room_booking.notification_service.enums.NotificationType;
import java.util.List;

public interface NotificationSubscriptionService {

  void subscribe(NotificationSubscriptionDto dto);

  List<NotificationType> getSubscriptions(Long userId);

}
