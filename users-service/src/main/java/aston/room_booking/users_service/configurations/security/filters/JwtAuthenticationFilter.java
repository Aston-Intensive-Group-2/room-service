package aston.room_booking.users_service.configurations.security.filters;

import aston.room_booking.users_service.configurations.security.components.JwtTokenProvider;

import aston.room_booking.users_service.configurations.security.CustomAuthenticationManager;
import aston.room_booking.users_service.utils.StaticConstants;
import aston.room_booking.users_service.utils.exceptions.AuthorizationException;
import aston.room_booking.users_service.utils.exceptions.TokenValidationException;
import aston.room_booking.users_service.utils.exceptions.UserNotFoundException;
import io.swagger.v3.oas.models.PathItem;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Класс-фильтр, отвечает за обработку BearerToken-запросов по токену
 * <br/>
 * Извлекает токен из заголовка авторизации;
 * <br/>
 * выполняетего дешифровку и валидацию.
 * <br/>
 * И помещенает  объекта {@code Authentication}  в контекст безопасности {@link SecurityContextHolder}
 *
 * @version 1.0
 * @author 4ndr33w
 */
@Component
@AllArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final CustomAuthenticationManager authenticationManager;

    /**
     * Метод-фильтр.
     * <p>
     * Метод:
     * <ul>
     *   <li>Извлекает из заголовка авторизации запроса {@code HttpServletRequest} токен;</li>
     *   <li>отправляет токен на валидацию;</li>
     *   <li>В случае успешной валидациитокена - запрашивает объект {@code Authentication},
     *   <br/>
     *   в котором содержится информация о пользователе и его полномочиях;</li>
     *   <li>помещает {@code Authentication} в контекст {@code SecurityContextHolder};</li>
     *   <li>вызывает следующий фильтр в цепочке обработки запроса.</li>
     * </ul>
     * <hr/>
     * </p>
     * @param request HttpRequest
     * @param response HttpResponse
     * @param filterChain цепочка фильтров
     *
     * @throws ServletException ошибка при обработке сервлета
     * @throws IOException ошибка входных данных
     * @throws TokenValidationException ошибка валидации токена
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException, TokenValidationException {

        String requestURI = request.getRequestURI();

        // КОСТЫЛЬ !
        // пришлось вводить дополнительную логику
        // после внедрения Swagger перестал работать SecurityFilterChain

        // Не проходили запросы к эндпойнтам:
        // -> документации OpenApi
        // -> Swagger
        // -> /api/v1/users POST-запрос на создание пользователя
        // не смотря на разрешение в списке SecurityFilterChain
        if (shouldSkip(requestURI, request)) {
            filterChain.doFilter(request, response);
            return;
        }

        if (request.getHeader("Authorization") == null || request.getHeader("Authorization").isEmpty()) {
            throw new AuthorizationException(StaticConstants.BASIC_AUTHORIZATION_HEADER_IS_MISSING_EXCEPTION_MESSAGE);
        }

        try {
            String token = jwtTokenProvider.resolveToken(request);
            Authentication authentication = authenticationManager.getAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (Exception e) {
            if(e.getMessage().equals(StaticConstants.USER_NOT_FOUND_EXCEPTION_MESSAGE)) {
                logger.error("%s; %s".formatted(StaticConstants.USER_NOT_FOUND_EXCEPTION_MESSAGE, e.getMessage()));
                throw new UserNotFoundException(StaticConstants.USER_NOT_FOUND_EXCEPTION_MESSAGE);
            }
            else {
                logger.error("%s; %s".formatted(StaticConstants.TOKEN_VALIDATION_EXCEPTION_MESSAGE, e.getMessage()));
                throw new TokenValidationException(StaticConstants.TOKEN_VALIDATION_EXCEPTION_MESSAGE);
            }
        }

        filterChain.doFilter(request, response);
    }

    private boolean shouldSkip(String requestURI, HttpServletRequest request) {
        return requestURI.contains("/swagger-ui") ||
                requestURI.contains("/v3/api-docs") ||
                (requestURI.contains("/api/v1/users") &&
                        request.getMethod().equals(HttpMethod.POST.toString()));
    }
}
