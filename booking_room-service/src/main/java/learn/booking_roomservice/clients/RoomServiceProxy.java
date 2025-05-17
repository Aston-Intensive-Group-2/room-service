package learn.booking_roomservice.clients;

import learn.booking_roomservice.dto.RoomDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient
public interface RoomServiceProxy {
    @GetMapping("/api/v1/rooms")
    ResponseEntity<RoomDTO> getRoom(Long roomId);
}
