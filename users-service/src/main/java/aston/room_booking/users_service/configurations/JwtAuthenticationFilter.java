package aston.room_booking.users_service.configurations;

import aston.room_booking.users_service.components.JwtTokenProvider;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
@Configuration
@AllArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;

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
     * @param request
     * @param response
     * @param filterChain
     *
     * @throws ServletException ошибка при обработке сервлета
     * @throws IOException ошибка входных данных
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String token = jwtTokenProvider.resolveToken(request);
        if(token!=null && jwtTokenProvider.validateToken(token)){

            Authentication authentication = jwtTokenProvider.getAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        filterChain.doFilter(request,response);
    }
}
