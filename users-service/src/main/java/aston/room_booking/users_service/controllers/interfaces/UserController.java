package aston.room_booking.users_service.controllers.interfaces;

import aston.room_booking.users_service.models.dtos.UserDto;
import aston.room_booking.users_service.models.entities.User;

public interface UserController extends BaseController<UserDto, User> {

    UserDto getByEmail (String email);
    UserDto getByUserName (String userName);

    boolean changeEmail (String newEmail, long id);
    boolean changePassword (String newPassword, long id);
}
