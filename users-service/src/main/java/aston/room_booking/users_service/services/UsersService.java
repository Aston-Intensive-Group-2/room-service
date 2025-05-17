package aston.room_booking.users_service.services;

import aston.room_booking.users_service.models.dtos.MessageDto;
import aston.room_booking.users_service.models.dtos.UserDto;
import aston.room_booking.users_service.models.entities.User;
import aston.room_booking.users_service.models.enums.UserRole;
import aston.room_booking.users_service.repositories.interfaces.UserRepository;
import aston.room_booking.users_service.services.interfaces.UserService;
import aston.room_booking.users_service.utils.PasswordHash;
import aston.room_booking.users_service.utils.StaticConstants;
import aston.room_booking.users_service.utils.exceptions.*;
import aston.room_booking.users_service.utils.mappers.UserMapper;
import aston.room_booking.users_service.utils.mappers.UserUpdater;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import java.util.Date;

/**
 *Сервис, реализующий интерфейс {@link UserService}
 * <p>
 *     Содержит операции по редактированию пользователем своего профиля
 *     <br/>
 *     при успешной аутенфикации по JWT-токену
 * </p>
 *
 * @version 1.0
 * @author 4ndr33w
 *
 * @see UserService
 * @see UserDto
 * @see User
 */
@Slf4j
@Service
@AllArgsConstructor
public class UsersService implements UserService<UserDto, User> {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordHash passwordHash;
    private final UserUpdater userUpdater;

    @Override
    public UserDto get()
            throws UserNotFoundException,
            ErrorFetchingUserDataException {

        long userId = getUserIdFromSecurityContext();
        var existingUserOptional = userRepository.findById(userId);

        if(existingUserOptional.isPresent()) {
            return userMapper.toDto(existingUserOptional.get());
        }
        else {
            log.warn(StaticConstants.USER_NOT_FOUND_EXCEPTION_MESSAGE);
            throw new UserNotFoundException(StaticConstants.USER_NOT_FOUND_EXCEPTION_MESSAGE);
        }
    }

    @Override
    public MessageDto delete() throws UserNotFoundException {

        long userId = getUserIdFromSecurityContext();
        try {
            userRepository.deleteById(userId);
            return new MessageDto(new Date().toString(),StaticConstants.SUCCESSFUL_USER_DELETE_MESSAGE);
        }
        catch (Exception e) {
            log.warn(StaticConstants.USER_NOT_FOUND_EXCEPTION_MESSAGE);
            throw new UserNotFoundException(StaticConstants.USER_NOT_FOUND_EXCEPTION_MESSAGE, e);
        }
    }

    @Override
    public UserDto create(User user)
            throws DatabaseOperationException,
            ArgumentIsNullException,
            ErrorFetchingUserDataException {

        if(user == null) {
            log.warn(StaticConstants.ARGUMENT_IS_NULL_EXCEPTION_MESSAGE);
            throw new ArgumentIsNullException(StaticConstants.ARGUMENT_IS_NULL_EXCEPTION_MESSAGE);
        }

        String hashedPassword = passwordHash.createHash(user.getPassword());
        user.setPassword(hashedPassword);
        user.setUserRole(UserRole.USER);

        //------------------------------------------------------------------
        // Выяснилось, что при создании нового пользователя
        // и добавлении в json поля "id"
        // обновляется существующий пользователь с указанным "id"
        // Таки образом можно изменить у существующего пользователя:
        // email; username; пароль; и задать роль USER (даже если он был ADMIN)
        // Так как в этом методе этого контроллера
        // всем принудительно ставитьс роль USER
        user.setId(null);
        //------------------------------------------------------------------

        try {
            return userMapper.toDto(userRepository.save(user));
        }
        catch (Exception e) {
            log.error(StaticConstants.DATABASE_ACCESS_EXCEPTION_MESSAGE);
            throw new DatabaseOperationException(StaticConstants.DATABASE_ACCESS_EXCEPTION_MESSAGE, e);
        }
    }

    @Override
    public UserDto update(User updatingUser)
            throws ArgumentIsNullException,
            ErrorFetchingUserDataException{

        if(updatingUser == null) {
            log.warn(StaticConstants.ARGUMENT_IS_NULL_EXCEPTION_MESSAGE);
            throw new ArgumentIsNullException(StaticConstants.ARGUMENT_IS_NULL_EXCEPTION_MESSAGE);
        }
        var updatedUser = userUpdater.simpleUserUpdate(updatingUser);
        var result = userRepository.save(updatedUser);
        return userMapper.toDto(result);
    }

    private long getUserIdFromSecurityContext() {

        var user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return user.getId();
    }
}
