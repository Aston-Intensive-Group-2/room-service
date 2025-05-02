package aston.room_booking.users_service.utils.mappers;

import aston.room_booking.users_service.models.dtos.UserDto;
import aston.room_booking.users_service.models.entities.User;
import aston.room_booking.users_service.utils.StaticConstants;
import aston.room_booking.users_service.utils.exceptions.ErrorFetchingUserDataException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 *
 * Утильный класс-маппер;
 * <br/>
 * Осуществляет преобразование
 * <br/>
 * {@code entity} -> {@code DTO}
 * <br/>
 * {@code DTO} -> {@code entity}
 *
 * @version 1.0
 * @author 4ndr33w
 */
@Slf4j
@Component
public class UserMapper {

    public UserDto toDto(User user) {
        try {
            return new UserDto(
                    user.getId(),
                    user.getUsername(),
                    user.getEmail(),
                    user.getFirstName(),
                    user.getLastName(),
                    user.getPhone(),
                    user.getUserRole(),
                    user.getImage(),
                    user.getCreatedAt(),
                    user.getLastLoginDate()
            );
        }
        catch (Exception e) {
            log.error(StaticConstants.UNABLE_TO_FETCH_USER_EXCEPTION_MESSAGE, e);
            throw new ErrorFetchingUserDataException(StaticConstants.UNABLE_TO_FETCH_USER_EXCEPTION_MESSAGE, e);
        }
    }

    public User toEntity(UserDto userDto, String password, Date updatedAt) {
        return User.builder()
                .id(userDto.id())
                .userName(userDto.userName())
                .email(userDto.email())
                .firstName(userDto.firstName())
                .lastName(userDto.lastName())
                .password(password)
                .phone(userDto.phone())
                .userRole(userDto.userRole())
                .createdAt(userDto.createdAt())
                .updatedAt(updatedAt)
                .lastLoginDate(userDto.lastLoginDate())
                .build();
    }
}
