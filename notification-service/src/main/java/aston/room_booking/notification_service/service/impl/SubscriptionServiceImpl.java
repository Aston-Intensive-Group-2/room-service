package aston.room_booking.notification_service.service.impl;

import aston.room_booking.notification_service.enums.NotificationType;
import aston.room_booking.notification_service.model.NotificationSubscription;
import aston.room_booking.notification_service.repository.NotificationSubscriptionRepository;
import aston.room_booking.notification_service.service.SubscriptionService;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SubscriptionServiceImpl implements SubscriptionService {

  private final NotificationSubscriptionRepository subscriptionRepository;

  @Transactional(readOnly = true)
  @Override
  public Set<NotificationType> getSubscriptionsForUser(Long userId) {
    return subscriptionRepository.findByUserId(userId).stream()
        .map(NotificationSubscription::getNotificationType)
        .collect(Collectors.toSet());
  }

  @Transactional
  @Override
  public void updateSubscriptions(Long userId, Set<NotificationType> types) {
    subscriptionRepository.deleteAll(subscriptionRepository.findByUserId(userId));
    List<NotificationSubscription> subscriptions = types.stream()
        .map(type -> {
          NotificationSubscription subscription = new NotificationSubscription();
          subscription.setId(userId);
          subscription.setNotificationType(type);
          return subscription;
        }).toList();
    subscriptionRepository.saveAll(subscriptions);
  }
}
