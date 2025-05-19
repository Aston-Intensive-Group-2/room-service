package aston.room_booking.notification_service.repository;

import aston.room_booking.notification_service.model.NotificationSubscription;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationSubscriptionRepository extends JpaRepository<NotificationSubscription, Long> {

  List<NotificationSubscription> findByUserId(Long userId);
}
