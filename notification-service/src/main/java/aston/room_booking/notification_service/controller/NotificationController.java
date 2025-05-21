package aston.room_booking.notification_service.controller;

import aston.room_booking.notification_service.dto.BookingWithUserDTO;
import aston.room_booking.notification_service.dto.NotificationSubscriptionDto;
import aston.room_booking.notification_service.enums.NotificationType;
import aston.room_booking.notification_service.service.NotificationService;
import aston.room_booking.notification_service.service.NotificationSubscriptionService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/notifications")
@RequiredArgsConstructor
public class NotificationController {

  private final NotificationService notificationService;
  private final NotificationSubscriptionService subscriptionService;

  @GetMapping("/hello")
  public String hello() {
    return "Hello from Notification Controller!";
  }

  @PostMapping
  public void receiveBooking(@RequestBody BookingWithUserDTO bookingDto) {
    notificationService.notify(bookingDto);
  }

//  @GetMapping("/history/{userId}")
//  public ResponseEntity<List<NotificationDto>> getUserNotifications(@PathVariable Long userId) {
//    return ResponseEntity.ok(notificationService.getUserNotifications(userId));
//  }

  @PostMapping("/subscribe")
  public void subscribe(@RequestBody NotificationSubscriptionDto dto) {
    subscriptionService.subscribe(dto);
  }

  @GetMapping("/subscriptions/{userId}")
  public List<NotificationType> getSubscriptions(@PathVariable Long userId) {
    return subscriptionService.getSubscriptions(userId);
  }
}
