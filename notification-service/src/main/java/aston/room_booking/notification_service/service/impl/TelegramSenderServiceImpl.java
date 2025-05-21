package aston.room_booking.notification_service.service.impl;

import aston.room_booking.notification_service.configuration.TelegramConfig;
import aston.room_booking.notification_service.dto.NotificationDto;
import aston.room_booking.notification_service.enums.NotificationType;
import aston.room_booking.notification_service.service.NotificationSenderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
@RequiredArgsConstructor
public class TelegramSenderServiceImpl implements NotificationSenderService {

  private final TelegramConfig telegramConfig;
  private final RestTemplate restTemplate;

  @Override
  public void sendNotification(NotificationDto notification) {
    String text = notification.message(); // важно — должен быть сформирован заранее

    if (text.isEmpty()) {
      log.warn("Сообщение для телеграм не задано");
      return;
    }

    String url = String.format(
        "https://api.telegram.org/bot%s/sendMessage?chat_id=%s&text=%s",
        telegramConfig.getBotToken(),
        telegramConfig.getChatId(),
        text.replace(" ", "+") // или URLEncoder, если сложный текст
    );

    try {
      restTemplate.getForObject(url, String.class);
      log.info("Уведомление отправлено в Telegram");
    } catch (Exception e) {
      log.error("Ошибка отправки в Telegram: {}", e.getMessage(), e);
    }
  }

  @Override
  public NotificationType getNotificationType() {
    return NotificationType.TELEGRAM;
  }
}
