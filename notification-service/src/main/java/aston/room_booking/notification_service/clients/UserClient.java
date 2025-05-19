package aston.room_booking.notification_service.clients;

import aston.room_booking.notification_service.dto.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "users-service")
public interface UserClient {
  @GetMapping("/api/v1/users")
  ResponseEntity<UserDto> getUser(@RequestHeader("Authorization") String token);
}
