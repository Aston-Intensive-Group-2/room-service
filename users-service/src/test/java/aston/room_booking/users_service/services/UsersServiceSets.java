package aston.room_booking.users_service.services;

import aston.room_booking.users_service.models.dtos.MessageDto;
import aston.room_booking.users_service.models.dtos.UserDto;
import aston.room_booking.users_service.models.entities.User;
import aston.room_booking.users_service.repositories.interfaces.UserRepository;
import aston.room_booking.users_service.utils.PasswordHash;
import aston.room_booking.users_service.utils.StaticConstants;
import aston.room_booking.users_service.utils.TestUtils;
import aston.room_booking.users_service.utils.exceptions.*;
import aston.room_booking.users_service.utils.mappers.UserMapper;
import aston.room_booking.users_service.utils.mappers.UserUpdater;
import jdk.jfr.Description;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

/**
 * @author 4ndr33w
 * @version 1.0
 */
@RunWith(MockitoJUnitRunner.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class UsersServiceSets extends TestUtils {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordHash passwordHash;

    @Mock
    private UserMapper userMapper;

    @Mock
    private UserUpdater userUpdater;

    @InjectMocks
    UsersService usersService;

    //---------------------------------------------------------------
    // Get Tests
    //---------------------------------------------------------------
    @Test
    @Description("Успешный поиск пользователя")
    public void testGet_UserFound_ReturnsDto() {

        setSecurityContextHolder(testUser1);

        when((SecurityContextHolder.getContext().getAuthentication().getPrincipal())).thenReturn(testUser1);

        when(userRepository.findById(testUser1.getId())).thenReturn(Optional.of(testUser1));
        when(userMapper.toDto(testUser1)).thenReturn(testUserDto1);

        UserDto result = usersService.get();

        assertNotNull(result);
        assertEquals(testUserDto1.id(), result.id());
        assertEquals(testUserDto1.userName(), result.userName());
        assertEquals(testUserDto1.email(), result.email());

        verify(userRepository).findById(testUser1.getId());
        verify(userMapper).toDto(testUser1);
    }

    @Test(expected = UserNotFoundException.class)
    @Description("Репозиторий вернул пустой Optional")
    public void testGet_UserNotFound_ReturnsException() {

        setSecurityContextHolder(testUser1);

        when((SecurityContextHolder.getContext().getAuthentication().getPrincipal())).thenReturn(testUser1);

        when(userRepository.findById(testUser1.getId()))
                .thenReturn(Optional.empty());

        usersService.get();

        verify(userRepository).findById(testUser1.getId());
    }

    @Test(expected = ErrorFetchingUserDataException.class)
    @Description("Ошибка парсинга User to UserDto")
    public void testGet_ErrorFetchinToDto_ReturnsException() {

        setSecurityContextHolder(testUser1);
        when((SecurityContextHolder.getContext().getAuthentication().getPrincipal())).thenReturn(testUser1);

        when(userRepository.findById(testUser1.getId())).thenReturn(Optional.of(testUser1));
        when(userMapper.toDto(testUser1))
                .thenThrow(new ErrorFetchingUserDataException(StaticConstants.UNABLE_TO_FETCH_USER_EXCEPTION_MESSAGE));

        usersService.get();

        verify(userRepository).findById(testUser1.getId());
        verify(userMapper).toDto(testUser1);
    }

    //---------------------------------------------------------------
    // Get Tests
    //---------------------------------------------------------------

    //---------------------------------------------------------------
    // Delete Tests
    //---------------------------------------------------------------

    @Test
    @Description("Успешное удаление пользователя")
    public void testDelete_Success() {
        setSecurityContextHolder(testUser1);

        when((SecurityContextHolder.getContext().getAuthentication().getPrincipal())).thenReturn(testUser1);

        doNothing().when(userRepository).deleteById(testUser1.getId());
        MessageDto result = usersService.delete();

        Assert.assertNotNull(result);
        assertTrue(result.message().contains(StaticConstants.SUCCESSFUL_USER_DELETE_MESSAGE));

        verify(userRepository, times(1)).deleteById(testUser1.getId());
    }

    @Test(expected = UserNotFoundException.class)
    @Description("Пользователь не найден; Ожидается UserNotFoundException")
    public void testDelete_UserNotFound() {

        setSecurityContextHolder(testUser1);

        when((SecurityContextHolder.getContext().getAuthentication().getPrincipal())).thenReturn(testUser1);

        doThrow(new EmptyResultDataAccessException(1))
                .when(userRepository)
                .deleteById(testUser1.getId());

        try {
            usersService.delete();
        } catch (UserNotFoundException e) {

            assertEquals(StaticConstants.USER_NOT_FOUND_EXCEPTION_MESSAGE, e.getMessage());
            throw e;
        }
    }

    //---------------------------------------------------------------
    // Delete Tests
    //---------------------------------------------------------------


    //---------------------------------------------------------------
    // Update Tests
    //---------------------------------------------------------------

    @Test(expected = ArgumentIsNullException.class)
    public void testUpdate_NullUser() {
        setSecurityContextHolder(testUser1);

        //when((SecurityContextHolder.getContext().getAuthentication().getPrincipal())).thenReturn(testUser1);
        try {
            usersService.update(null);
        } catch (ArgumentIsNullException e) {
            assertEquals(StaticConstants.ARGUMENT_IS_NULL_EXCEPTION_MESSAGE, e.getMessage());
            throw e;
        }
    }

    @Test
    public void testUpdate_Success() {
        setSecurityContextHolder(testUser1);

        //when((SecurityContextHolder.getContext().getAuthentication().getPrincipal())).thenReturn(testUser1);

        when(userUpdater.simpleUserUpdate(testUser1)).thenReturn(testUser1);
        when(userRepository.save(testUser1)).thenReturn(testUser1);
        when(userMapper.toDto(testUser1)).thenReturn(testUserDto1);

        UserDto result = usersService.update(testUser1);

        assertNotNull(result);
        assertEquals(testUserDto1, result);
    }

    @Test(expected = ErrorFetchingUserDataException.class)
    public void testUpdate_ErrorParseToDto() {
        setSecurityContextHolder(testUser1);

        //when((SecurityContextHolder.getContext().getAuthentication().getPrincipal())).thenReturn(testUser1);

        when(userUpdater.simpleUserUpdate(testUser1)).thenReturn(testUser1);
        when(userRepository.save(testUser1)).thenReturn(testUser1);

        when(userMapper.toDto(testUser1))
                .thenThrow(new ErrorFetchingUserDataException(StaticConstants.UNABLE_TO_FETCH_USER_EXCEPTION_MESSAGE));

        usersService.update(testUser1);
    }

    //---------------------------------------------------------------
    // Update Tests
    //---------------------------------------------------------------

    //---------------------------------------------------------------
    // Create Tests
    //---------------------------------------------------------------


    @Test(expected = ArgumentIsNullException.class)
    @Description("на вход передаётся null")
    public void testCreate_NullUser_ThrowsException() {

        usersService.create(null);
    }

    @Test
    @Description("Пользователь успешно создаётся")
    public void testCreate_ValidUser_Success() {

        Mockito.when(passwordHash.createHash(Mockito.anyString()))
                .thenReturn(testHashedPassword);

        Mockito.when(userRepository.save(Mockito.any(User.class)))
                .thenReturn(testUser1);

        Mockito.when(userMapper.toDto(testUser1))
                .thenReturn(testUserDto1);

        UserDto result = usersService.create(testUser1);

        assertEquals(testUserDto1, result);
        assertEquals(testHashedPassword, testUser1.getPassword());

        Mockito.verify(passwordHash).createHash(Mockito.anyString());
        Mockito.verify(userRepository).save(testUser1);
        Mockito.verify(userMapper).toDto(testUser1);
    }


    @Test(expected = DatabaseOperationException.class)
    @Description("Ошибка при обращении к репозиторию")
    public void testCreate_DatabaseError_ThrowsException() {

        Mockito.when(passwordHash.createHash(Mockito.anyString()))
                .thenReturn(testHashedPassword);

        Mockito.when(userRepository.save(Mockito.any(User.class)))
                .thenThrow(new DatabaseOperationException(StaticConstants.DATABASE_ACCESS_EXCEPTION_MESSAGE));

        usersService.create(testUser1);
    }

    @Test
    @Description("Проверка работы хэша пароля")
    public void testCreate_PasswordHash_CalledCorrectly() {

        Mockito.when(passwordHash.createHash(Mockito.anyString()))
                .thenReturn(testHashedPassword);

        Mockito.when(userRepository.save(Mockito.any(User.class)))
                .thenReturn(testUser1);

        Mockito.when(userMapper.toDto(testUser1))
                .thenReturn(testUserDto1);

        usersService.create(testUser1);

        Mockito.verify(passwordHash).createHash(Mockito.anyString());
    }
    //---------------------------------------------------------------
    // Create Tests
    //---------------------------------------------------------------
}
