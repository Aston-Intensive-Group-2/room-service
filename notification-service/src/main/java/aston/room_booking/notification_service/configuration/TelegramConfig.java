package aston.room_booking.notification_service.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
@ConfigurationProperties(prefix = "telegram")
public class TelegramConfig {

  private String botToken;
  private String chatId;


}
