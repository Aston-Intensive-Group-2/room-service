package aston.room_booking.users_service.utils;

import aston.room_booking.users_service.models.dtos.UserDto;
import aston.room_booking.users_service.models.entities.User;
import aston.room_booking.users_service.models.enums.UserRole;
import aston.room_booking.users_service.utils.mappers.UserMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author 4ndr33w
 * @version 1.0
 */
public class TestUtils {

    private UserMapper mapper = new UserMapper();

    public UserDto testUserDto1 = new UserDto(
            1,
            "Vasyan",
            "test@example.com",
            "Vasiliy",
            "Pupkin",
            "1234567890",
            UserRole.USER,
            null,
            new Date(),
            new Date()
    );

    public UserDto testUserDto1_Updated = new UserDto(
            1,
            "Vasyan",
            "test@example.com",
            "Vasiliy111",
            "Pupkin111",
            "1234567890111",
            UserRole.USER,
            null,
            new Date(),
            new Date()
    );

    public String testEmailCaps1 = "TEST@example.com";

    public UserDto testUserDto2 = new UserDto(
            2,
            "Andr33w",
            "Andr33w@mail.com",
            "Andrew",
            "123",
            "1234567890",
            UserRole.USER,
            null,
            new Date(),
            new Date()
    );

    public UserDto testUserDto3 = new UserDto(
            3,
            "Andr33w11",
            "Andr3311w@mail.com",
            "Andrew",
            "123",
            "1234567890",
            UserRole.USER,
            null,
            new Date(),
            new Date()
    );

    public User testUser1 = mapper.toEntity(testUserDto1, "$2a$10$J/St0YBoPPVUtroUE4o9ZOqmmmpjU6k14hvq.TfDQlcjsp5dUutNG", new Date());

    public User testUser1_Updated = mapper.toEntity(testUserDto1_Updated, "$2a$10$J/St0YBoPPVUtroUE4o9ZOqmmmpjU6k14hvq.TfDQlcjsp5dUutNG", new Date());

    public User testUser2 = mapper.toEntity(testUserDto2, "$2a$10$J/St0YBoPPVUtroUE4o9ZOqmmmpjU6k14hvq.TfDQlcjsp5dUutNG", new Date());

    public User testUser3 = mapper.toEntity(testUserDto3, "$2a$10$J/St0YBoPPVUtroUE4o9ZOqmmmpjU6k14hvq.TfDQlcjsp5dUutNG", new Date());

    //var userDtos = List.of(testUserDto1, testUserDto2, testUserDto3);

    public String testHashedPassword = "$2a$10$J/St0YBoPPVUtroUE4o9ZOqmmmpjU6k14hvq.TfDQlcjsp5dUutNG";

    public void setSecurityContextHolder(User user) {

        //var user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        //return user.getId();

        Authentication authentication = mock(Authentication.class);


        SecurityContext securityContext = mock(SecurityContext.class);

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(user);
        SecurityContextHolder.setContext(securityContext);
    }
}
