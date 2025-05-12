package aston.room_booking.users_service.services;

import aston.room_booking.users_service.models.dtos.MessageDto;
import aston.room_booking.users_service.models.dtos.UserDto;
import aston.room_booking.users_service.models.entities.User;
import aston.room_booking.users_service.models.enums.UserRole;
import aston.room_booking.users_service.repositories.interfaces.UserRepository;
import aston.room_booking.users_service.services.interfaces.AdminService;
import aston.room_booking.users_service.utils.PasswordHash;
import aston.room_booking.users_service.utils.StaticConstants;
import aston.room_booking.users_service.utils.exceptions.*;
import aston.room_booking.users_service.utils.mappers.UserMapper;
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

    @Override
    public UserDto create(User user)
            throws EmailAlreadyUseException,
            DatabaseOperationException,
            ArgumentIsNullException {

        if(user == null) {
            log.warn(StaticConstants.ARGUMENT_IS_NULL_EXCEPTION_MESSAGE);
            throw new ArgumentIsNullException(StaticConstants.ARGUMENT_IS_NULL_EXCEPTION_MESSAGE);
        }
        if(userRepository.findByEmail(user.getEmail()).isPresent()) {
            log.warn(StaticConstants.EMAIL_IS_ALREADY_IN_USE_EXCEPTION_MESSAGE);
            throw new EmailAlreadyUseException(StaticConstants.EMAIL_IS_ALREADY_IN_USE_EXCEPTION_MESSAGE);
        }

        String hashedPassword = passwordHash.createHash(user.getPassword());
        user.setPassword(hashedPassword);

        var savedUser = userRepository.save(user);

        if(savedUser != null) {
            return userMapper.toDto(user);
        }
        else {
            log.error(StaticConstants.DATABASE_ACCESS_EXCEPTION_MESSAGE);
            throw new DatabaseOperationException(StaticConstants.DATABASE_ACCESS_EXCEPTION_MESSAGE);
        }
    }

    @Override
    public Collection<UserDto> getAll() throws NoUsersFoundException, ErrorFetchingUserDataException {

        var users = userRepository.findAll();
        if (!users.isEmpty()) {
            return users.stream().map(userMapper::toDto).toList();
        }
        log.warn(StaticConstants.NO_USERS_FOUND_EXCEPTION_MESSAGE);
        throw new NoUsersFoundException(StaticConstants.NO_USERS_FOUND_EXCEPTION_MESSAGE);

    }

    @Override
    public UserDto getByEmail(String email) throws UserNotFoundException, ErrorFetchingUserDataException, ArgumentIsNullException {

        if(email == null) {
            log.warn(StaticConstants.ARGUMENT_IS_NULL_EXCEPTION_MESSAGE);
            throw new ArgumentIsNullException(StaticConstants.ARGUMENT_IS_NULL_EXCEPTION_MESSAGE);
        }
        var userOptional = userRepository.findByEmail(email);

        if(userOptional.isEmpty()) {
            log.warn(StaticConstants.USER_NOT_FOUND_EXCEPTION_MESSAGE);
            throw new UserNotFoundException(StaticConstants.USER_NOT_FOUND_EXCEPTION_MESSAGE);
        }
        return userMapper.toDto(userOptional.get());
    }

    @Override
    public UserDto getById(long id) {
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
            DatabaseOperationException {

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
    public UserDto update(long id, User user) throws UserNotFoundException, ErrorFetchingUserDataException, ArgumentIsNullException {
        if(user == null) {
            log.warn(StaticConstants.ARGUMENT_IS_NULL_EXCEPTION_MESSAGE);
            throw new ArgumentIsNullException(StaticConstants.ARGUMENT_IS_NULL_EXCEPTION_MESSAGE);
        }

        var existingUserOptional = userRepository.findById(id);

        if(existingUserOptional.isEmpty()) {
            log.warn(StaticConstants.USER_NOT_FOUND_EXCEPTION_MESSAGE);
            throw new UserNotFoundException(StaticConstants.USER_NOT_FOUND_EXCEPTION_MESSAGE);
        }
        var existingUser = existingUserOptional.get();

        var updatedUser = getUpdatedUser(user, existingUser);

        var result = userRepository.save(updatedUser);

        if(result == null) {
            throw new DatabaseOperationException(StaticConstants.DATABASE_ACCESS_EXCEPTION_MESSAGE);
        }
        return userMapper.toDto(result);
    }

    private User getUpdatedUser(User updatingData, User existingUser) {

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
}