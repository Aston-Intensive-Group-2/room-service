package aston.room_booking.notification_service.clients;

import aston.room_booking.notification_service.dto.RoomDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "room-service")
public interface RoomClient {

  @GetMapping("/api/v1/rooms/{id}")
  RoomDto getRoomById(@PathVariable("id") Long roomId);

}
