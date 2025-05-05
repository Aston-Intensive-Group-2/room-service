package aston.room_booking.users_service.configurations.security;

import aston.room_booking.users_service.configurations.security.components.JwtTokenProvider;
import aston.room_booking.users_service.configurations.security.filters.LoginAuthenticationFilter;
import aston.room_booking.users_service.utils.PasswordHash;
import aston.room_booking.users_service.models.entities.User;
import aston.room_booking.users_service.utils.StaticConstants;
import aston.room_booking.users_service.utils.exceptions.AuthenticationException;

import aston.room_booking.users_service.utils.exceptions.InvalidUsernameOrPasswordException;
import aston.room_booking.users_service.utils.exceptions.TokenValidationException;
import aston.room_booking.users_service.utils.exceptions.UserNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;

/**
 *
 * @version 1.0
 * @author 4ndr33w
 */
@Slf4j
@Configuration
@AllArgsConstructor
public class CustomAuthenticationManager implements AuthenticationManager {

    private final PasswordHash passwordHash;
    private final JwtTokenProvider tokenProvider;

    /**
     * Метод вызывается из метода
     * <br/>
     * {@link LoginAuthenticationFilter#attemptAuthentication(HttpServletRequest, HttpServletResponse)},
     * <br/>
     * вызываемого в {@link SecurityFilterChainConfig#filterChain(HttpSecurity)};
     * <p>
     *     Менеджер аутенфикации - метод:
     * <ul>
     *   <li>Извлекает сущность {@code user} и {@code password} из переданного в качестве параметра объекта {@code Authentication};</li>
     *   <li>Осуществляет валидацию хэша пароля;</li>
     *   <li>В случае успешной валидации: создаёт объект {@code UsernamePasswordAuthenticationToken}, в который помещаются {@code user} и список ролей;</li>
     * </ul>
     * </p>
     * @param authentication
     * @return {@code UsernamePasswordAuthenticationToken} - контейнер, содержащий объект {@code user} и список ролей
     *
     * @throws AuthenticationException
     */
    @Override
    public Authentication authenticate(Authentication authentication) throws InvalidUsernameOrPasswordException {

        try {
            String password = authentication.getName();
            var user = (User) authentication.getCredentials();

            boolean isPasswordValid = passwordHash.checkHash(password, user.getPassword());

            if(isPasswordValid) {
                var authorities = user.getAuthorities();

                return new UsernamePasswordAuthenticationToken(user, null, authorities);
            }
            else {
                log.warn(StaticConstants.INVALID_USERNAME_OR_PASSWORD_EXCEPTION_MESSAGE);
                throw new InvalidUsernameOrPasswordException(StaticConstants.INVALID_USERNAME_OR_PASSWORD_EXCEPTION_MESSAGE) {
                };
            }
        }
        catch (Exception e) {
            throw new InvalidUsernameOrPasswordException(StaticConstants.INVALID_USERNAME_OR_PASSWORD_EXCEPTION_MESSAGE);
        }
    }

    /**
     * Метод возвращает объект-контейнер {@code Authentication},
     * <br/>
     * Содержащий объект {@code User}, а так же набор ролей
     * <p>
     *     На входе принимает токен;
     *     <br/>
     *     Осуществляет валидацию токена и поиск пользователя
     *     <br/>
     *     по извлечённому из токена {@code email}
     *     <br/>
     *     Найденный пользователь помещается в контейнер {@code Authentication}
     *     <br/>
     *     вместе со списком ролей
     * </p>
     *
     * @param token JWT-токен
     * @return {@code Authentication} - объект-контейнер, содержащий информацию для авторизации
     *
     * @throws UserNotFoundException
     * @throws TokenValidationException
     */
    public Authentication getAuthentication(String token) throws TokenValidationException, UserNotFoundException {

        var userOptional = tokenProvider.validateTokenAndReturnOptionalOfUser(token);

        if(userOptional.isPresent()) {
            var user = userOptional.get();
            return new UsernamePasswordAuthenticationToken(user, "", List.of(user.getUserRole()));
        }
        throw new UserNotFoundException(StaticConstants.USER_NOT_FOUND_EXCEPTION_MESSAGE);
    }
}
