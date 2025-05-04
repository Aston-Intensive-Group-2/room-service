package aston.room_booking.users_service.configurations.filters;

import aston.room_booking.users_service.models.dtos.ErrorDto;
import aston.room_booking.users_service.utils.exceptions.handlers.ExceptionResolver;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Класс-фильр перехвата ошибок, возникающих в процессе аутенфикации
 * <br/>
 * (т.е. до попадания в контроллер и перехвата их обработчиком исключений контроллера)
 *
 * @author 4ndr33w
 * @version 1.0
 */
@Configuration
@AllArgsConstructor
public class ExceptionHndlerFilter extends OncePerRequestFilter {

    private final ObjectMapper objectMapper;
    private final ExceptionResolver exceptionExceptionResolver;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        try {
            filterChain.doFilter(request, response);
        }
        catch (Exception e) {
            var errorDto = exceptionExceptionResolver.handleException(e);
            printResponse(response, errorDto);
        }
    }

    private void printResponse(HttpServletResponse response, ErrorDto errorDto) throws IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(errorDto.statusCode());

        var jsonResponse = objectMapper.writeValueAsString(errorDto);
        response.getWriter().write(jsonResponse);
    }
}
