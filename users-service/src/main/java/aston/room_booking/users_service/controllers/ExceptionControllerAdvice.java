package aston.room_booking.users_service.controllers;

import aston.room_booking.users_service.models.dtos.ErrorDto;
import aston.room_booking.users_service.utils.exceptions.handlers.ExceptionResolver;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * Класс-перехватчик исключений
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
     * @param e оболочка выпавшего исключения
     * @return {@code ErrorDto} - ответ, сожержащий код ошибки, дату и сообщение
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleException(Exception e) {
        ErrorDto errorDto = exceptionExceptionResolver.handleException(e);
        return ResponseEntity.status(errorDto.statusCode()).body(errorDto);
    }
}
