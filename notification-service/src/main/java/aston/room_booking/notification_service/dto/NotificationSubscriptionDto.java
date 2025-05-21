package aston.room_booking.notification_service.dto;

import aston.room_booking.notification_service.enums.NotificationType;

public record NotificationSubscriptionDto(Long userId, NotificationType notificationType) {

}
