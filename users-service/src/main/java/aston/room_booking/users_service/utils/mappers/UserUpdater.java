package aston.room_booking.users_service.utils.mappers;

import aston.room_booking.users_service.models.entities.User;
import aston.room_booking.users_service.models.enums.UserRole;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/**
 * Класс для парсинга обновлённых пользовательских данных
 * Необходимость вынесения логики из сервиса
 * возникла при написании тестов.
 * <br/>
 * Локально (приватным методом в сервисе) не отрабатывал в тестах корректно
 * и, соответственно, процент покрытия тестами сильно уменьшался
 *
 * @author 4ndr33w
 * @version 1.0
 */
@Component
public class UserUpdater {

    public User updateUserWithAdminPrivilegies(User updatingData, User existingUser) {

        String newUserName = updatingData.getUsername() == null? existingUser.getUsername() : updatingData.getUsername();
        String newFirstName =  updatingData.getFirstName() == null? existingUser.getFirstName() : updatingData.getFirstName();
        String newLastName = updatingData.getLastName() == null? existingUser.getLastName() : updatingData.getLastName();
        String newPhone = updatingData.getPhone() == null? existingUser.getPhone() : updatingData.getPhone();
        byte[] newImage = updatingData.getImage() == null? existingUser.getImage() : updatingData.getImage();
        String newEmail = updatingData.getEmail() == null? existingUser.getEmail() : updatingData.getEmail();
        UserRole newRole = updatingData.getUserRole() == existingUser.getUserRole()? existingUser.getUserRole() : updatingData.getUserRole();

        existingUser.setUserName(newUserName);
        existingUser.setFirstName(newFirstName);
        existingUser.setLastName(newLastName);
        existingUser.setPhone(newPhone);
        existingUser.setImage(newImage);
        existingUser.setEmail(newEmail);
        existingUser.setUserRole(newRole);

        return existingUser;
    }

    public User simpleUserUpdate(User userDto) {

        var existingUser = getUserFromSecurityContext();
        String newFirstName =  userDto.getFirstName() == null? existingUser.getFirstName() : userDto.getFirstName();
        String newLastName = userDto.getLastName() == null? existingUser.getLastName() : userDto.getLastName();
        String newPhone = userDto.getPhone() == null? existingUser.getPhone() : userDto.getPhone();
        byte[] newImage = userDto.getImage() == null? existingUser.getImage() : userDto.getImage();

        existingUser.setFirstName(newFirstName);
        existingUser.setLastName(newLastName);
        existingUser.setPhone(newPhone);
        existingUser.setImage(newImage);

        return existingUser;
    }

    private User getUserFromSecurityContext() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
