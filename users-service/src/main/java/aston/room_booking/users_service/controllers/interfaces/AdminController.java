package aston.room_booking.users_service.controllers.interfaces;

import aston.room_booking.users_service.models.dtos.UserDto;
import aston.room_booking.users_service.models.entities.User;
import org.springframework.http.ResponseEntity;

import java.util.Collection;

public interface AdminController extends BaseController<UserDto, User>{
    ResponseEntity<?> getAll();
    boolean changeRole (String newRole, long id);
}
