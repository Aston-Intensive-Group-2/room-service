package aston.room_booking.notification_service.service.impl;

import aston.room_booking.notification_service.dto.NotificationDto;
import aston.room_booking.notification_service.enums.NotificationType;
import aston.room_booking.notification_service.service.NotificationSenderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailSenderServiceImpl implements NotificationSenderService {

  private final JavaMailSender javaMailSender;
  @Value("${notification.email.from}")
  private String senderEmail;

  @Override
  public void sendNotification(NotificationDto notification) {
    SimpleMailMessage message = new SimpleMailMessage();
    message.setFrom(senderEmail);
    message.setTo(notification.email());
    message.setSubject("Уведомление о бронировании");
    message.setText(notification.message());

    log.info("EMAIL sending notification");

    try {
      javaMailSender.send(message);
      log.info("Email sent to {}.", notification.email());
    } catch (MailException e) {
      log.error("Ошибка при отправке email пользователю {}: {}", notification.email(),
          e.getMessage(), e);
      throw new RuntimeException(e);
    }
  }

  @Override
  public NotificationType getNotificationType() {
    return NotificationType.EMAIL;
  }
}
