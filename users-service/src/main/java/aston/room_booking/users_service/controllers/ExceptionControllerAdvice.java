package aston.room_booking.users_service.controllers;

import aston.room_booking.users_service.models.dtos.ErrorDto;
import aston.room_booking.users_service.utils.exceptions.handlers.ExceptionResolver;
import aston.room_booking.users_service.configurations.security.filters.ExceptionHndlerFilter;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * Класс-перехватчик исключений, проброшенных в контроллеры
 *
 * @author 4ndr33w
 * @version 1.0
 */
@ControllerAdvice
@AllArgsConstructor
public class ExceptionControllerAdvice {

    private final ExceptionResolver exceptionExceptionResolver;

    /**
     * Метод перехватывает исключения в контроллерах, отправляет на опознание в утильный метод
     * </br/>
     * Возвращает ответ, оформленный в {@code ErrorDto}
     *
     * <p>
     *     Перехватывает только исключения, выпавшие после успешной аутенфикации
     *     <br/>
     *     Так как при ошибках аутенфикации запрос даже не дойдёт до контроллеров
     * </p>
     * <p>
     *     Перехватом исключений в сервлетах {@code spring.security} занимается другой обработчик исключений,
     *     выполненный в виде фильтра в цепочке {@code SecurityFilterChainConfig}
     * </p>
     *
     * @param e оболочка выпавшего исключения
     * @return {@code ErrorDto} - ответ, сожержащий код ошибки, дату и сообщение
     *
     * @see ExceptionHndlerFilter
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleException(Exception e) {
        ErrorDto errorDto = exceptionExceptionResolver.handleException(e);
        return ResponseEntity.status(errorDto.statusCode()).body(errorDto);
    }
}
