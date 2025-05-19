package aston.room_booking.notification_service.service;

import aston.room_booking.notification_service.enums.NotificationType;
import java.util.Set;

public interface SubscriptionService {

  Set<NotificationType> getSubscriptionsForUser(Long userId);

  void updateSubscriptions(Long userId, Set<NotificationType> types);

}
