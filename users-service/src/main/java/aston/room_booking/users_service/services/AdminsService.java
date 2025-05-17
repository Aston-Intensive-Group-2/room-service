package aston.room_booking.users_service.services;

import aston.room_booking.users_service.models.dtos.MessageDto;
import aston.room_booking.users_service.models.dtos.UserDto;
import aston.room_booking.users_service.models.entities.User;
import aston.room_booking.users_service.repositories.interfaces.UserRepository;
import aston.room_booking.users_service.services.interfaces.AdminService;
import aston.room_booking.users_service.utils.PasswordHash;
import aston.room_booking.users_service.utils.StaticConstants;
import aston.room_booking.users_service.utils.exceptions.*;
import aston.room_booking.users_service.utils.mappers.UserMapper;
import aston.room_booking.users_service.utils.mappers.UserUpdater;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Date;

/**
 *Сервис, реализующий интерфейс {@link AdminService}
 * <p>
 *     Содержит административные операции по работе с пользовательскими данными
 *     <br/>
 *     если у авторизованного пользователя  роль {@code ADMIN}
 * </p>
 *
 * @version 1.0
 * @author 4ndr33w
 *
 * @see AdminService
 * @see UserDto
 * @see User
 */
@Slf4j
@Service
@AllArgsConstructor
public class AdminsService implements AdminService<UserDto, User> {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordHash passwordHash;
    private final UserUpdater userUpdater;

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
        user.setEmail(user.getEmail().toLowerCase());

        //------------------------------------------------------------------
        // Выяснилось, что при создании нового пользователя
        // и добавлении в json поля "id"
        // обновляется существующий пользователь с указанным "id"
        // Таки образом можно изменить у существующего пользователя:
        // email; username и пароль
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
    public Collection<UserDto> getAll()
            throws NoUsersFoundException,
            ErrorFetchingUserDataException {

        var users = userRepository.findAll();
        if (!users.isEmpty()) {
            return users.stream().map(userMapper::toDto).toList();
        }

        log.warn(StaticConstants.NO_USERS_FOUND_EXCEPTION_MESSAGE);
        throw new NoUsersFoundException(StaticConstants.NO_USERS_FOUND_EXCEPTION_MESSAGE);
    }

    @Override
    public UserDto getByEmail(String email)
            throws UserNotFoundException,
            ErrorFetchingUserDataException,
            ArgumentIsNullException {

        if(email == null) {
            log.warn(StaticConstants.ARGUMENT_IS_NULL_EXCEPTION_MESSAGE);
            throw new ArgumentIsNullException(StaticConstants.ARGUMENT_IS_NULL_EXCEPTION_MESSAGE);
        }

        var userOptional = userRepository.findByEmail(email.toLowerCase());

        if(userOptional.isEmpty()) {
            log.warn(StaticConstants.USER_NOT_FOUND_EXCEPTION_MESSAGE);
            throw new UserNotFoundException(StaticConstants.USER_NOT_FOUND_EXCEPTION_MESSAGE);
        }
        return userMapper.toDto(userOptional.get());
    }

    @Override
    public UserDto getById(long id)
            throws UserNotFoundException,
            InvalidArgumentException,
            ErrorFetchingUserDataException{

        if(id < 0) {
            throw new InvalidArgumentException(StaticConstants.INVALID_ARGUMENT_EXCEPTION_MESSAGE);
        }
        var userOptional = userRepository.findById(id);

        if(userOptional.isPresent()) {
            return userMapper.toDto(userOptional.get());
        }
        else {
            log.warn(StaticConstants.USER_NOT_FOUND_EXCEPTION_MESSAGE);
            throw new UserNotFoundException(StaticConstants.USER_NOT_FOUND_EXCEPTION_MESSAGE);
        }
    }

    @Override
    public MessageDto deleteById(long id)
            throws UserNotFoundException,
            InvalidArgumentException,
            DatabaseOperationException {

        if(id < 0) {
            throw new InvalidArgumentException(StaticConstants.INVALID_ARGUMENT_EXCEPTION_MESSAGE);
        }

        var existingUserOptional = userRepository.findById(id);

        if(existingUserOptional.isPresent()) {
            try {
                userRepository.deleteById(id);
                return new MessageDto(new Date().toString(),StaticConstants.SUCCESSFUL_USER_DELETE_MESSAGE);
            }
            catch (Exception e) {
                log.error(StaticConstants.DATABASE_ACCESS_EXCEPTION_MESSAGE);
                throw new DatabaseOperationException(StaticConstants.UNABLE_TO_DELETE_USER_EXCEPTION_MESSAGE);
            }
        }
        log.warn(StaticConstants.USER_NOT_FOUND_EXCEPTION_MESSAGE);
        throw new UserNotFoundException(StaticConstants.USER_NOT_FOUND_EXCEPTION_MESSAGE);
    }

    @Override
    public UserDto updateById(long id, User user)
            throws UserNotFoundException,
            ErrorFetchingUserDataException,
            InvalidArgumentException,
            ArgumentIsNullException,
            DatabaseOperationException {
        if(user == null) {
            log.warn(StaticConstants.ARGUMENT_IS_NULL_EXCEPTION_MESSAGE);
            throw new ArgumentIsNullException(StaticConstants.ARGUMENT_IS_NULL_EXCEPTION_MESSAGE);
        }
        if(id < 0) {
            throw new InvalidArgumentException(StaticConstants.INVALID_ARGUMENT_EXCEPTION_MESSAGE);
        }

        var existingUserOptional = userRepository.findById(id);

        if(existingUserOptional.isEmpty()) {
            log.warn(StaticConstants.USER_NOT_FOUND_EXCEPTION_MESSAGE);
            throw new UserNotFoundException(StaticConstants.USER_NOT_FOUND_EXCEPTION_MESSAGE);
        }
        try {
            var existingUser = existingUserOptional.get();

            var updatedUser = userUpdater.updateUserWithAdminPrivilegies(user, existingUser);

            var result = userRepository.save(updatedUser);

            return userMapper.toDto(result);
        }
        catch (Exception e) {
            log.error(StaticConstants.DATABASE_ACCESS_EXCEPTION_MESSAGE);
            throw new DatabaseOperationException(StaticConstants.DATABASE_ACCESS_EXCEPTION_MESSAGE, e);
        }
    }
}