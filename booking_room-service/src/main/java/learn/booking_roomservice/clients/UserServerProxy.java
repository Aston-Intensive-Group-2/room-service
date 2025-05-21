package learn.booking_roomservice.clients;

import learn.booking_roomservice.dto.UserDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "users-service")
public interface UserServerProxy {
    @GetMapping("/api/v1/users")
    ResponseEntity<UserDTO> get(@RequestHeader("Authorization") String token);
}

