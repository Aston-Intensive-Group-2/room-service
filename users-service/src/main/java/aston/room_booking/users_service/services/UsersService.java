package aston.room_booking.users_service.services;

import aston.room_booking.users_service.models.dtos.UserDto;
import aston.room_booking.users_service.models.entities.User;
import aston.room_booking.users_service.models.enums.UserRole;
import aston.room_booking.users_service.repositories.interfaces.UserRepository;
import aston.room_booking.users_service.services.interfaces.BaseService;
import aston.room_booking.users_service.utils.PasswordHash;
import aston.room_booking.users_service.utils.StaticConstants;
import aston.room_booking.users_service.utils.exceptions.*;
import aston.room_booking.users_service.utils.mappers.UserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Slf4j
@Service
@RequiredArgsConstructor
public class UsersService implements BaseService<UserDto, User> {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordHash passwordHash;

    @Override
    public UserDto create(User user) throws EmailAlreadyUseException, DatabaseOperationException, ArgumentIsNullException, ErrorFetchingUserDataException {
        if(user == null) {
            log.warn(StaticConstants.ARGUMENT_IS_NULL_EXCEPTION_MESSAGE);
            throw new ArgumentIsNullException(StaticConstants.ARGUMENT_IS_NULL_EXCEPTION_MESSAGE);
        }
        if(userRepository.findByEmail(user.getEmail()).isPresent()) {
            log.warn(StaticConstants.EMAIL_IS_ALREADY_IN_USE_EXCEPTION_MESSAGE);
            throw new EmailAlreadyUseException(StaticConstants.EMAIL_IS_ALREADY_IN_USE_EXCEPTION_MESSAGE);
        }

        String hashedPassword = passwordHash.createHash(user.getPassword());
        user.setUserRole(UserRole.USER);
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
    public UserDto getByUserName(String userName) throws UserNotFoundException, ErrorFetchingUserDataException, ArgumentIsNullException {
        if(userName== null) {
            log.warn(StaticConstants.ARGUMENT_IS_NULL_EXCEPTION_MESSAGE);
            throw new ArgumentIsNullException(StaticConstants.ARGUMENT_IS_NULL_EXCEPTION_MESSAGE);
        }
        var userOptional = userRepository.findByUserName(userName);

        if(userOptional.isEmpty()) {
            log.warn(StaticConstants.USER_NOT_FOUND_EXCEPTION_MESSAGE);
            throw new UserNotFoundException(StaticConstants.USER_NOT_FOUND_EXCEPTION_MESSAGE);
        }
        return userMapper.toDto(userOptional.get());
    }

    @Override
    public boolean deleteById(long id) throws UserNotFoundException {
        var existingUserOptional = userRepository.findById(id);

        if(existingUserOptional.isPresent()) {
            userRepository.deleteById(id);
            return true;
        }
        log.warn(StaticConstants.USER_NOT_FOUND_EXCEPTION_MESSAGE);
        throw new UserNotFoundException(StaticConstants.USER_NOT_FOUND_EXCEPTION_MESSAGE);
    }

    @Override
    public UserDto update(long id, UserDto userDto) throws UserNotFoundException, ErrorFetchingUserDataException, ArgumentIsNullException {
        if(userDto == null) {
            log.warn(StaticConstants.ARGUMENT_IS_NULL_EXCEPTION_MESSAGE);
            throw new ArgumentIsNullException(StaticConstants.ARGUMENT_IS_NULL_EXCEPTION_MESSAGE);
        }

        var existingUserOptional = userRepository.findById(id);

        if(existingUserOptional.isEmpty()) {
            log.warn(StaticConstants.USER_NOT_FOUND_EXCEPTION_MESSAGE);
            throw new UserNotFoundException(StaticConstants.USER_NOT_FOUND_EXCEPTION_MESSAGE);
        }
        var existingUser = existingUserOptional.get();

        var updatedUser = getUpdatedUser(id, userDto, existingUser);

        var result = userRepository.save(updatedUser);

        if(result == null) {
            throw new DatabaseOperationException(StaticConstants.DATABASE_ACCESS_EXCEPTION_MESSAGE);
        }
        return userMapper.toDto(result);
    }

    private User getUpdatedUser(long id, UserDto userDto, User existingUser) {

        String newFirstName =  userDto.firstName() == null? existingUser.getFirstName() : userDto.firstName();
        String newLastName = userDto.lastName() == null? existingUser.getLastName() : userDto.lastName();
        String newPhone = userDto.phone() == null? existingUser.getPhone() : userDto.phone();
        byte[] newImage = userDto.image() == null? existingUser.getImage() : userDto.image();

        existingUser.setFirstName(userDto.firstName());
        existingUser.setLastName(userDto.lastName());
        existingUser.setPhone(newPhone);
        existingUser.setImage(newImage);

        return existingUser;
    }
}