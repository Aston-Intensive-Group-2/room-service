package aston.room_booking.notification_service.service;


import aston.room_booking.notification_service.dto.BookingWithUserDTO;

public interface NotificationService {

  void notify(BookingWithUserDTO bookingDto);

//  List<NotificationDto> getUserNotifications(Long userId);
}
