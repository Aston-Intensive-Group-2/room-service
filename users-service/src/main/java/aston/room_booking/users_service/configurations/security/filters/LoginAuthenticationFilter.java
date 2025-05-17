package aston.room_booking.users_service.configurations.security.filters;

import aston.room_booking.users_service.configurations.security.components.JwtTokenProvider;
import aston.room_booking.users_service.configurations.security.CustomAuthenticationManager;
import aston.room_booking.users_service.configurations.security.SecurityFilterChainConfig;
import aston.room_booking.users_service.models.dtos.TokenDto;
import aston.room_booking.users_service.models.entities.User;
import aston.room_booking.users_service.repositories.interfaces.UserRepository;
import aston.room_booking.users_service.utils.StaticConstants;
import aston.room_booking.users_service.utils.exceptions.AuthorizationException;

import aston.room_booking.users_service.utils.exceptions.InvalidUsernameOrPasswordException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.Date;

/**
 * Класс-фильтр отвечает за обработку запросов аутенфикации по логину и паролю
 * <br/>
 * Осуществляет цепочку действий по перехвату и валидации введённых {@code email} и {@code password}
 * <br/>
 * Осуществляется поиск пользователя с соответствующими {@code email} и {@code password}
 * <br/>
 * Генерирует и возвращает JWT-токен на основе данных найденного пользователя
 *
 * @version 1.0
 * @author 4ndr33w
 */
@Slf4j
@AllArgsConstructor
public class LoginAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final UserRepository userRepository;
    private final CustomAuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    /**
     * Метод вызывается из {@link SecurityFilterChainConfig#filterChain(HttpSecurity, LoginAuthenticationFilter, JwtAuthenticationFilter, ExceptionHndlerFilter)}
     * <br/>
     * как фильтр для перехвата {@code email} и {@code password};
     * <p>
     *     метод:
     * <ul>
     *   <li>Извлекает {@code email} и {@code password} из переданного в качестве параметра объекта {@code Authentication};</li>
     *   <li>отправляет их на верификацию;</li>
     *   <li>В случае успешной валидации: получает объект {@code user};</li>
     *   <li>Создаёт объект-контейнер {@code UsernamePasswordAuthenticationToken}, содержащий {@code password} и {@code user};</li>
     * </ul>
     * </p>
     * @param request HttpRequest
     * @param response HttpResponse
     *
     * @return {@code Authentication} - объект-контейнер, содержащий логин и пароль
     *
     * @throws AuthenticationException Ошибка при аутенфикации
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        String authorizationHeader = request.getHeader("Authorization");

        if(authorizationHeader != null && authorizationHeader.startsWith("Basic ")) {

            var usernamePassword = getUsernamePasswordFromRequest(request);

            String login = usernamePassword[0];
            String enteredPassword = usernamePassword[1];

            var user = tryToGetUser(login);

            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(enteredPassword, user);

            return authenticationManager.authenticate(authenticationToken);
        }
        else {
            log.warn(StaticConstants.BASIC_AUTHORIZATION_HEADER_IS_MISSING_MESSAGE);
            throw new AuthorizationException(StaticConstants.BASIC_AUTHORIZATION_HEADER_IS_MISSING_MESSAGE);
        }
    }

    private String[] getUsernamePasswordFromRequest(HttpServletRequest request) {

        try {
            String base64Credentials = request.getHeader("Authorization").substring("Basic ".length());
            String credentials = new String(java.util.Base64.getDecoder().decode(base64Credentials));

            return credentials.split(":", 2);
        }
        catch (IllegalArgumentException e) {
            throw new AuthorizationException(e.getMessage());
        }
    }

    private User tryToGetUser(String email) {

        var user = userRepository.findByEmail(email);

        if (user.isPresent()) {
            return user.get();
        } else {
            log.error(StaticConstants.INVALID_USERNAME_OR_PASSWORD_EXCEPTION_MESSAGE);
            throw new InvalidUsernameOrPasswordException(StaticConstants.INVALID_USERNAME_OR_PASSWORD_EXCEPTION_MESSAGE);
        }
    }

    /**
     * Финальный Метод аутенфикации по логину и паролю;
     * <br/>
     * вызывается механизмом Spring-Security
     * <br/>
     * после успешной верификации {@code email} и {@code password}
     * <br/>
     * и нахождения в базе данных соответствующего пользователя
     * <br/>
     * Запрашивается генерация токена и возвращается {@code json} ответа
     * <br/>
     * Так же токен добавляется в заголовок авторизации
     *
     * @param request HttpRequest
     * @param response HttpResponse
     * @param chain цепочка фильтров
     * @param authResult объект аутенфикации, содержащий пользователя и список ролей
     *
     * @throws IOException ошибка ввода/вывода
     */
    @Override
    public void successfulAuthentication(HttpServletRequest request,
                                         HttpServletResponse response,
                                         FilterChain chain,
                                         Authentication authResult) throws IOException{

        User user = (User) authResult.getPrincipal();
        String token = jwtTokenProvider.createToken(user);

        TokenDto tokenDto = new TokenDto(user.getEmail(), user.getUserRole(), token, new Date().toString());

        String jsonResponse = new ObjectMapper().writeValueAsString(tokenDto);

        response.addHeader("Authorization", "Bearer " + token);
        response.setContentType("application/json");
        response.getWriter().write(jsonResponse);
    }

    /**
     * Метод передаёт упраление фильтру-перехватчику исключений
     * <br/>
     * в случае выброса исключения в процессе аутенфикации
     *
     * @param request HttpRequest
     * @param response HttpResponse
     * @param failed возникшая при аутенфикации ошибка
     */
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request,
                                              HttpServletResponse response,
                                              AuthenticationException failed) {

        throw failed;
    }
}
