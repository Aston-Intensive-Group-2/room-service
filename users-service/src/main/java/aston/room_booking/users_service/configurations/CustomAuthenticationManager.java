package aston.room_booking.users_service.configurations;

import aston.room_booking.users_service.utils.PasswordHash;
import aston.room_booking.users_service.models.entities.User;
import aston.room_booking.users_service.utils.StaticConstants;
import aston.room_booking.users_service.utils.exceptions.CustomAuthenticationException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 *
 * @version 1.0
 * @author 4ndr33w
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
public class CustomAuthenticationManager implements AuthenticationManager {

    private final PasswordHash passwordHash;

    /**
     * Метод вызывается из метода {@link AuthenticationFilter#attemptAuthentication(HttpServletRequest, HttpServletResponse)}, вызываемого механизмом Spring-Securiey;
     * <br/> туда же (в вызвавший метод) и возвращает результат, который вызывающим методом помещается в контекст безопасности {@link SecurityContextHolder}
     * <p>
     *     Менеджер аутенфикации - метод:
     * <ul>
     *   <li>Извлекает сущность {@code user} и {@code password} из переданного в качестве параметра объекта {@code Authentication};</li>
     *   <li>Осуществляет валижацию хэша пароля;</li>
     *   <li>В случае успешной валидации: создаёт объект {@code UsernamePasswordAuthenticationToken}, в который помещаются {@code user} и список ролей;</li>
     * </ul>
     * </p>
     * @param authentication
     * @return {@code UsernamePasswordAuthenticationToken} - контейнер, содержащий объект {@code user} и список ролей
     *
     * @throws AuthenticationException
     */
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

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
                throw new AuthenticationException(StaticConstants.INVALID_USERNAME_OR_PASSWORD_EXCEPTION_MESSAGE) {
                };
            }
        }
        catch (AuthenticationException e) {
            throw new CustomAuthenticationException(StaticConstants.INVALID_USERNAME_OR_PASSWORD_EXCEPTION_MESSAGE);
        }
    }
}
