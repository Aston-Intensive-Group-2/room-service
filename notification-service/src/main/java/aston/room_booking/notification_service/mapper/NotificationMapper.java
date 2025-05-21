package aston.room_booking.notification_service.mapper;

import aston.room_booking.notification_service.dto.NotificationDto;
import aston.room_booking.notification_service.model.Notification;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface NotificationMapper {

  NotificationMapper INSTANCE = Mappers.getMapper(NotificationMapper.class);

  NotificationDto toDto(Notification notification);

  Notification toEntity(NotificationDto notificationDto);

}
