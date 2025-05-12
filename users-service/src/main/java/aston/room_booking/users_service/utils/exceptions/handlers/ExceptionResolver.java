package aston.room_booking.users_service.utils.exceptions.handlers;

import aston.room_booking.users_service.models.dtos.ErrorDto;
import aston.room_booking.users_service.utils.StaticConstants;
import aston.room_booking.users_service.utils.exceptions.*;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.stereotype.Component;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Date;

/**
 * Утильный класс проверки выскочившего исключения и возврата контейнера с ответом
 *
 * <p>
 * Данный класс используется не только для обработки исключений из контроллеров,
 * <br/>
 * но и для обработки исключений в сервлетах аутенфикации
 * <br/>
 * ДО срабатывания обработчика исключений контроллеров {@link ControllerAdvice}
 * </p>
 *
 * @author 4ndr33w
 * @version 1.0
 */
@Slf4j
@Component
public class ExceptionResolver {

    /**
     * Класс проверяет какое именно исключение сработало
     * <br/>
     * Возвращает DTO-контейнер с ответом ({@code ErrorDto})
     * <br/>
     * содержит код ошибки, дату, сообщение
     *
     * @param exception
     * @return {@code ErrorDto} - ответ
     */
    public ErrorDto handleException(Exception exception) {

        var cause = exception.getCause();//getRootCause(exception);
        String message = exception.getMessage();

        int statusCode = HttpServletResponse.SC_INTERNAL_SERVER_ERROR;

        if(message.contains("No static resource")) {

            message = "No such static resource. Or missing path variable";
            statusCode = HttpServletResponse.SC_NOT_FOUND;
        }

        if (exception instanceof ArgumentIsNullException) {

            message = StaticConstants.ARGUMENT_IS_NULL_EXCEPTION_MESSAGE;
            statusCode = HttpServletResponse.SC_BAD_REQUEST;
        }
        if(exception instanceof MethodArgumentNotValidException) {
            message = ((MethodArgumentNotValidException) exception)
                    .getBindingResult()
                    .getAllErrors().stream()
                    .findFirst()
                    .map(this::resolveErrorMessage)
                    .orElse("Validation failed");
            statusCode = HttpServletResponse.SC_BAD_REQUEST;
        }
        if (exception instanceof AuthorizationException) {

            statusCode = HttpServletResponse.SC_BAD_REQUEST;
        }
        if (exception instanceof CustomAuthenticationException) {

            statusCode = HttpServletResponse.SC_BAD_REQUEST;
        }
        if (exception instanceof AuthorizationException) {

            statusCode = HttpServletResponse.SC_BAD_REQUEST;
        }
        if (exception instanceof DatabaseOperationException) {

            if (message.contains("duplicate key")) {
                message = StaticConstants.USER_ALREADY_EXISTS_EXCEPTION_MESSAGE;
                statusCode = HttpServletResponse.SC_BAD_REQUEST;
            } else {
                statusCode = HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
            }
        }
        if (exception instanceof DatabaseTransactionException) {

            statusCode = HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
        }
        if (exception instanceof EmailAlreadyUseException) {
            statusCode = HttpServletResponse.SC_CONFLICT;
            message = StaticConstants.EMAIL_IS_ALREADY_IN_USE_EXCEPTION_MESSAGE;
        }
        if (exception instanceof ErrorFetchingUserDataException) {
            statusCode = HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
            message = StaticConstants.UNABLE_TO_FETCH_USER_EXCEPTION_MESSAGE;
        }
        if (exception instanceof JwtException) {
            statusCode = HttpServletResponse.SC_EXPECTATION_FAILED;
            message = StaticConstants.JWT_VERIFICATION_EXCEPTION;
        }
        if (exception instanceof NoUsersFoundException) {
            statusCode = HttpServletResponse.SC_NOT_FOUND;
            message = StaticConstants.NO_USERS_FOUND_EXCEPTION_MESSAGE;
        }
        if (exception instanceof TokenValidationException) {
            statusCode = HttpServletResponse.SC_BAD_REQUEST;
            message = StaticConstants.TOKEN_VALIDATION_EXCEPTION_MESSAGE;
        }
        if (exception instanceof UserNotFoundException) {
            statusCode = HttpServletResponse.SC_NOT_FOUND;
            message = StaticConstants.USER_NOT_FOUND_EXCEPTION_MESSAGE;
        }
        if (exception instanceof InvalidUsernameOrPasswordException) {
            statusCode = HttpServletResponse.SC_BAD_REQUEST;
            message = StaticConstants.INVALID_USERNAME_OR_PASSWORD_EXCEPTION_MESSAGE;
        }
        if (exception instanceof AuthenticationException) {
            statusCode = HttpServletResponse.SC_BAD_REQUEST;
            message = StaticConstants.INVALID_USERNAME_OR_PASSWORD_EXCEPTION_MESSAGE;
        }
        if (exception instanceof HttpMessageNotReadableException) {
            statusCode = HttpServletResponse.SC_BAD_REQUEST;
            if(exception.getMessage().contains("Required request body is missing")) {
                message = "Required request body is missing";
            }
            else {
                message = exception.getMessage();
            }
        }
        if (exception instanceof NullPointerException) {
            statusCode = HttpServletResponse.SC_BAD_REQUEST;
            if(exception.getMessage().contains(StaticConstants.ARGUMENT_IS_NULL_EXCEPTION_MESSAGE)) {
                message = StaticConstants.ARGUMENT_IS_NULL_EXCEPTION_MESSAGE;
            }
            else {
                message = exception.getMessage();
            }
        }
        if (exception instanceof ForbiddenOperationException) {
            statusCode = HttpServletResponse.SC_FORBIDDEN;
            if(exception.getMessage().contains(StaticConstants.FORBIDDEN_OPERATION_EXCEPTION_MESSAGE)) {
                message = StaticConstants.FORBIDDEN_OPERATION_EXCEPTION_MESSAGE;
            }
            else {
                message = exception.getMessage();
            }
        }
        if (exception instanceof AuthorizationDeniedException) {
            statusCode = HttpServletResponse.SC_FORBIDDEN;
            if(exception.getMessage().contains(StaticConstants.FORBIDDEN_OPERATION_EXCEPTION_MESSAGE)) {
                message = "Access Denied";
            }
            else {
                message = exception.getMessage();
            }
        }
        if (exception instanceof IOException) {
            statusCode = HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
            message = StaticConstants.UNEXPECTED_ERROR_MESSAGE;
        }
        if (exception instanceof SQLException) {
            statusCode = HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
            message = StaticConstants.UNEXPECTED_ERROR_MESSAGE;
        }

        log.error(message);

        return new ErrorDto(
                new Date().toString(),
                statusCode,
                message );
    }
/*
    private Throwable getRootCause(Throwable ex) {
        Throwable cause = ex;
        while (cause.getCause() != null && cause.getCause() != cause) {
            cause = cause.getCause();
        }
        return cause;
    }*/
    private String resolveErrorMessage(ObjectError error) {
        if (error instanceof FieldError fieldError) {
            return fieldError.getDefaultMessage();
        }
        return error.getDefaultMessage();
    }
}
