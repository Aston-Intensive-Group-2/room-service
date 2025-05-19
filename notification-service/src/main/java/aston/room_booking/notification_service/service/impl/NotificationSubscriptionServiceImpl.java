package aston.room_booking.notification_service.service.impl;

import aston.room_booking.notification_service.dto.NotificationSubscriptionDto;
import aston.room_booking.notification_service.enums.NotificationType;
import aston.room_booking.notification_service.model.NotificationSubscription;
import aston.room_booking.notification_service.repository.NotificationSubscriptionRepository;
import aston.room_booking.notification_service.service.NotificationSubscriptionService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationSubscriptionServiceImpl implements NotificationSubscriptionService {

  private final NotificationSubscriptionRepository repository;

  @Override
  public void subscribe(NotificationSubscriptionDto dto) {
    NotificationSubscription sub = new NotificationSubscription();
    sub.setUserId(dto.userId());
    sub.setNotificationType(dto.notificationType());
    repository.save(sub);
  }

  @Override
  public List<NotificationType> getSubscriptions(Long userId) {
    return repository.findByUserId(userId).stream()
        .map(NotificationSubscription::getNotificationType).toList();
  }
}
