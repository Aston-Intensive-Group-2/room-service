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
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.doNothing;

/**
 * Тестовый класс.
 * <br/>
 * Тестирует методы сервиса {@link AdminsService}
 *
 * @author 4ndr33w
 * @version 1.0
 */
@RunWith(MockitoJUnitRunner.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class AdminsServiceTest extends TestUtils {
    @InjectMocks
    AdminsService adminsService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordHash passwordHash;

    @Mock
    private UserMapper userMapper;

    @Mock
    private UserUpdater userUpdater;

    //---------------------------------------------------------------
    // Create Tests
    //---------------------------------------------------------------

    @Test(expected = ArgumentIsNullException.class)
    @Description("на вход передаётся null")
    public void testCreate_NullUser_ThrowsException() {
        adminsService.create(null);
    }

    @Test
    @Description("Пользователь успешно создаётся")
    public void testCreate_ValidUser_Success() {

        Mockito.when(passwordHash
                .createHash(Mockito.anyString()))
                .thenReturn(testHashedPassword);

        Mockito.when(userRepository.save(Mockito.any(User.class))).thenReturn(testUser1);
        Mockito.when(userMapper.toDto(testUser1)).thenReturn(testUserDto1);

        UserDto result = adminsService.create(testUser1);

        assertEquals(testUserDto1, result);
        assertEquals(testHashedPassword, testUser1.getPassword());

        Mockito.verify(passwordHash)
                .createHash(testHashedPassword);

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

        adminsService.create(testUser1);
    }

    @Test
    @Description("Проверка работы хэша пароля")
    public void testCreate_PasswordHash_CalledCorrectly() {

        Mockito.when(passwordHash.createHash(Mockito.anyString())).thenReturn(testHashedPassword);
        Mockito.when(userRepository.save(Mockito.any(User.class))).thenReturn(testUser1);
        Mockito.when(userMapper.toDto(testUser1)).thenReturn(testUserDto1);

        adminsService.create(testUser1);

        Mockito.verify(passwordHash).createHash(Mockito.anyString());
    }
    //---------------------------------------------------------------
    // Create Tests
    //---------------------------------------------------------------

    //---------------------------------------------------------------
    // GetAll Tests
    //---------------------------------------------------------------

    @Test
    @Description("Успешно получаем коллекцию пользовательских DTO")
    public void testGetAll_UsersFound_Success() {

        Mockito.when(userRepository.findAll())
                .thenReturn(List.of(testUser1, testUser2));

        Mockito.when(userMapper.toDto(testUser1))
                .thenReturn(testUserDto1);

        Mockito.when(userMapper.toDto(testUser2))
                .thenReturn(testUserDto2);

        Collection<UserDto> result = adminsService.getAll();

        assertEquals(2, result.size());
        assertTrue(result.contains(testUserDto1));
        assertTrue(result.contains(testUserDto2));

        Mockito.verify(userRepository).findAll();
        Mockito.verify(userMapper).toDto(testUser1);
        Mockito.verify(userMapper).toDto(testUser2);
    }

    @Test(expected = ErrorFetchingUserDataException.class)
    @Description("Ошибка парсинга пользователя в DTO")
    public void testGetAll_ErrorFetchingUsers_ThrowsException() {

        Mockito.when(userRepository.findAll())
                .thenReturn(List.of(testUser1, testUser2));

        Mockito.when(userMapper.toDto(testUser1))
                .thenReturn(testUserDto1);

        Mockito.when(userMapper.toDto(testUser2))
                .thenThrow(new ErrorFetchingUserDataException(StaticConstants.UNABLE_TO_FETCH_USER_EXCEPTION_MESSAGE));

        adminsService.getAll();
    }

    @Test
    @Description("Успешно получаем единичный элемент коллекции")
    public void testGetAll_SingleUser_Success() {

        Mockito.when(userRepository.findAll())
                .thenReturn(List.of(testUser1));

        Mockito.when(userMapper.toDto(testUser1))
                .thenReturn(testUserDto1);

        Collection<UserDto> result = adminsService.getAll();

        assertEquals(1, result.size());
        assertTrue(result.contains(testUserDto1));

        Mockito.verify(userRepository).findAll();
        Mockito.verify(userMapper).toDto(testUser1);
    }

    @Test(expected = NoUsersFoundException.class)
    @Description("Репозиторий вернул пустой список")
    public void testGetAll_ReturnEmptyList() {

        Mockito.when(userRepository.findAll())
                .thenReturn(List.of());

        Collection<UserDto> result = adminsService.getAll();

        Mockito.verify(userRepository).findAll();
    }

    //---------------------------------------------------------------
    // GetAll Tests
    //---------------------------------------------------------------

    //---------------------------------------------------------------
    // GetByEmail Tests
    //---------------------------------------------------------------

    @Test(expected = ArgumentIsNullException.class)
    @Description("Переданный на вход email = null; получаем ArgumentIsNullException")
    public void testGetByEmail_NullEmail_ThrowsException() {

        adminsService.getByEmail(null);
    }

    @Test
    @Description("Успешный поиск по email")
    public void testGetByEmail_UserFound_Success() {

        Mockito.when(userRepository.findByEmail(Mockito.anyString()))
                .thenReturn(Optional.of(testUser1));

        Mockito.when(userMapper.toDto(testUser1))
                .thenReturn(testUserDto1);

        UserDto result = adminsService.getByEmail(Mockito.anyString());

        assertEquals(testUserDto1, result);

        Mockito.verify(userRepository).findByEmail(Mockito.anyString());
        Mockito.verify(userMapper).toDto(testUser1);
    }

    @Test(expected = UserNotFoundException.class)
    @Description("Пользователь с указанным email не найден; получаем UserNotFoundException")
    public void testGetByEmail_UserNotFound_ThrowsException() {

        Mockito.when(userRepository.findByEmail(Mockito.anyString()))
                .thenReturn(Optional.empty());

        adminsService.getByEmail(Mockito.anyString());
    }

    @Test(expected = ErrorFetchingUserDataException.class)
    @Description("Ошибка маппинга в DTO; получаем ErrorFetchingUserDataException")
    public void testGetByEmail_DatabaseError_ThrowsException() {

        Mockito.when(userRepository.findByEmail(Mockito.anyString()))
                .thenReturn(Optional.of(testUser1));

        Mockito.when(userMapper.toDto(testUser1))
                .thenThrow(new ErrorFetchingUserDataException(StaticConstants.UNABLE_TO_FETCH_USER_EXCEPTION_MESSAGE));

        adminsService.getByEmail(Mockito.anyString());
    }

    @Test
    @Description("Проверка чуствительности email к регистру")
    public void testGetByEmail_EmailCaseInsensitive_Success() {

        Mockito.when(userRepository.findByEmail(testUserDto1.email()))
                .thenReturn(Optional.of(testUser1));

        Mockito.when(userMapper.toDto(testUser1))
                .thenReturn(testUserDto1);

        UserDto result = adminsService.getByEmail(testEmailCaps1);

        assertEquals(testUserDto1, result);

        Mockito.verify(userRepository).findByEmail(testUserDto1.email());
        Mockito.verify(userMapper).toDto(testUser1);
    }

    //---------------------------------------------------------------
    // GetByEmail Tests
    //---------------------------------------------------------------

    //---------------------------------------------------------------
    // GetById Tests
    //---------------------------------------------------------------

    @Test
    @Description("Успешный поиск пользователя по Id")
    public void testGetById_UserFound_Success() {

        Mockito.when(userRepository.findById(testUser1.getId()))
                .thenReturn(Optional.of(testUser1));
        Mockito.when(userMapper.toDto(testUser1))
                .thenReturn(testUserDto1);

        UserDto result = adminsService.getById(testUser1.getId());

        assertEquals(testUserDto1, result);

        Mockito.verify(userRepository).findById(testUser1.getId());
        Mockito.verify(userMapper).toDto(testUser1);
    }

    @Test(expected = UserNotFoundException.class)
    @Description("Пользователь не найден")
    public void testGetById_UserNotFound_ThrowsException() {

        Mockito.when(userRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.empty());

        adminsService.getById(Mockito.anyLong());
    }

    @Test(expected = ErrorFetchingUserDataException.class)
    @Description("Ошибка маппинга в DTO; получаем ErrorFetchingUserDataException")
    public void testGetById_ErrorFetchingUser_ThrowsException() {

        Mockito.when(userRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(testUser1));

        Mockito.when(userMapper.toDto(testUser1))
                .thenThrow(new ErrorFetchingUserDataException(StaticConstants.UNABLE_TO_FETCH_USER_EXCEPTION_MESSAGE));

        adminsService.getById(Mockito.anyLong());
    }

    @Test(expected = InvalidArgumentException.class)
    @Description("Переданный на вход Id отрицательный; получаем  InvalidArgumentException")
    public void testGetById_NegativeId_ThrowsException() {

        long userId = -1L;

        UserDto result = adminsService.getById(userId);
    }

    //---------------------------------------------------------------
    // GetById Tests
    //---------------------------------------------------------------


    //---------------------------------------------------------------
    // DeleteById Tests
    //---------------------------------------------------------------

    @Test
    @Description("Успешное удаление пользователя")
    public void testDeleteById_Success() {

        Mockito.when(userRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(testUser1));

        MessageDto result = adminsService.deleteById(Mockito.anyLong());

        assertNotNull(result);
        assertTrue(result.message().contains(StaticConstants.SUCCESSFUL_USER_DELETE_MESSAGE));

        Mockito.verify(userRepository).findById(Mockito.anyLong());
        Mockito.verify(userRepository).deleteById(Mockito.anyLong());
    }

    @Test(expected = UserNotFoundException.class)
    @Description("Пользователь не найден; Ожидается UserNotFoundException")
    public void testDeleteById_UserNotFound() {

        Mockito.when(userRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.empty());

        adminsService.deleteById(Mockito.anyLong());
    }

    @Test(expected = DatabaseOperationException.class)
    @Description("Ошибка при обращении к репозиторию")
    public void testDeleteById_DatabaseError() {

        Mockito.when(userRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(testUser1));
        Mockito.doThrow(new DatabaseOperationException(StaticConstants.DATABASE_ACCESS_EXCEPTION_MESSAGE))
                .when(userRepository).deleteById(Mockito.anyLong());

        adminsService.deleteById(Mockito.anyLong());
    }

    @Test(expected = InvalidArgumentException.class)
    @Description("Переданный на вход Id отрицательный; получаем  InvalidArgumentException")
    public void testDeleteById_NegativeId_throwsException() {

        long userId = -1L;
        var result = adminsService.deleteById(userId);
    }

    //---------------------------------------------------------------
    // DeleteById Tests
    //---------------------------------------------------------------

    //---------------------------------------------------------------
    // UpdateById Tests
    //---------------------------------------------------------------

    @Test(expected = ArgumentIsNullException.class)
    @Description("Переданный на вход user равен null; получаем  ArgumentIsNullException")
    public void testUpdateById_NullUser_ThrowsException() {

        adminsService.updateById(1L, null);
    }

    @Test
    @Description("Успешное обновление пользователя")
    public void testUpdateById_Success() {

        Mockito.when(userRepository.findById(testUser1.getId()))
                .thenReturn(Optional.of(testUser1));

        Mockito.when(userUpdater.updateUserWithAdminPrivilegies(testUser1_Updated, testUser1))
                        .thenReturn(testUser1_Updated);

        Mockito.when(userRepository.save(testUser1_Updated))
                .thenReturn(testUser1_Updated);

        Mockito.when(userMapper.toDto(testUser1_Updated))
                .thenReturn(testUserDto1_Updated);

        UserDto result = adminsService.updateById(testUser1.getId(), testUser1_Updated);

        assertEquals(testUserDto1_Updated, result);

        Mockito.verify(userRepository).findById(testUser1.getId());
        Mockito.verify(userRepository).save(testUser1_Updated);
        Mockito.verify(userMapper).toDto(testUser1_Updated);
    }

    @Test(expected = UserNotFoundException.class)
    @Description("Пользователь не найден")
    public void testUpdateById_UserNotFound_ThrowsException() {

        Mockito.when(userRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.empty());

        adminsService.updateById(Mockito.anyLong(), testUser1);
        Mockito.verify(userRepository).findById(Mockito.anyLong());
    }

    //@Ignore
    @Test(expected = DatabaseOperationException.class)
    @Description("Ошибка при возврате информации из репозитория")
    public void testUpdateById_DatabaseError_ThrowsException() {

        Mockito.when(userRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(testUser1));

        Mockito.when(userUpdater.updateUserWithAdminPrivilegies(testUser1_Updated, testUser1))
                .thenReturn(testUser1_Updated);

        Mockito.when(userRepository.save(testUser1_Updated))
                        .thenThrow(new DatabaseOperationException(StaticConstants.DATABASE_ACCESS_EXCEPTION_MESSAGE));

        adminsService.updateById(testUser1.getId(), testUser1_Updated);
    }

    @Test(expected = InvalidArgumentException.class)
    @Description("Переданный на вход Id отрицательный; получаем  InvalidArgumentException")
    public void testUpdateById_NegativeId_throwsException() {

        long userId = -1L;

        UserDto result = adminsService.updateById(userId, testUser1);
    }

    //---------------------------------------------------------------
    // UpdateById Tests
    //---------------------------------------------------------------
}
