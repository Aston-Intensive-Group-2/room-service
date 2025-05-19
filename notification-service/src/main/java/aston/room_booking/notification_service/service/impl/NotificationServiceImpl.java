package aston.room_booking.notification_service.service.impl;

import aston.room_booking.notification_service.clients.RoomClient;
import aston.room_booking.notification_service.dto.BookingWithUserDTO;
import aston.room_booking.notification_service.dto.NotificationDto;
import aston.room_booking.notification_service.dto.RoomDto;
import aston.room_booking.notification_service.dto.UserDto;
import aston.room_booking.notification_service.enums.NotificationType;
import aston.room_booking.notification_service.mapper.NotificationMapper;
import aston.room_booking.notification_service.repository.NotificationRepository;
import aston.room_booking.notification_service.service.NotificationMessageBuilderService;
import aston.room_booking.notification_service.service.NotificationSenderService;
import aston.room_booking.notification_service.service.NotificationService;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class NotificationServiceImpl implements NotificationService {

  private final Map<NotificationType, NotificationSenderService> senderMap;
  private final NotificationRepository notificationRepository;
  private final RoomClient roomClient;
  private final NotificationMessageBuilderService messageBuilder;

  public NotificationServiceImpl(
      List<NotificationSenderService> senderServiceList,
      NotificationRepository notificationRepository,
      RoomClient roomClient,
      NotificationMessageBuilderService messageBuilder) {
    this.senderMap = senderServiceList.stream()
        .collect(
            Collectors.toMap(NotificationSenderService::getNotificationType, Function.identity()));
    this.notificationRepository = notificationRepository;
    this.roomClient = roomClient;
    this.messageBuilder = messageBuilder;
  }

  @Transactional
  public void notify(BookingWithUserDTO bookingDto) {

    RoomDto roomDto = roomClient.getRoomById(bookingDto.roomId());
    log.info("ROOOM:" + roomDto.toString());

    NotificationDto notification = new NotificationDto(
        bookingDto.user().id(),
        bookingDto.roomId(),
        bookingDto.user().userName(),
        bookingDto.user().email(),
        bookingDto.user().phone(),
        bookingDto.start(),
        bookingDto.end(),
        bookingDto.status(),
        messageBuilder.buildMessage(roomDto, bookingDto)
    );

    for (NotificationType type : resolveSubscriptions(bookingDto.user())) {
      NotificationSenderService sender = senderMap.get(type);
      try {
        sender.sendNotification(notification);
      } catch (Exception e) {
        log.error("Ошибка при отправке уведомления через {} для пользователя {}",
            type, bookingDto.user().id(), e);
      }
    }
    notificationRepository.save(
        NotificationMapper.INSTANCE.toEntity(notification));
  }

//  @Override
//  public List<NotificationDto> getUserNotifications(Long userId) {
//    return notificationRepository.findByUserId(userId).stream()
//        .map(NotificationMapper.INSTANCE::toDto)
//        .collect(Collectors.toList());
//  }

  private Set<NotificationType> resolveSubscriptions(UserDto user) {
    // ВРЕМЕННО: если телефон есть — оба типа, если только email — один
    if (user.phone() != null && user.email() != null) {
      return EnumSet.of(NotificationType.EMAIL, NotificationType.TELEGRAM);
    } else if (user.phone() != null) {
      return EnumSet.of(NotificationType.TELEGRAM);
    } else {
      return EnumSet.of(NotificationType.EMAIL);
    }
  }
}
