package aston.room_booking.users_service.utils.exceptions.handlers;

import aston.room_booking.users_service.models.dtos.ErrorDto;
import aston.room_booking.users_service.utils.StaticConstants;
import aston.room_booking.users_service.utils.exceptions.*;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * Утильный класс проверки выскочившего исключения и возврата контейнера с ответом
 *
 * @author 4ndr33w
 * @version 1.0
 */
@Slf4j
@Component
public class Handler {


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

        var cause = exception.getCause();
        String message = exception.getMessage();
        if(cause != null) {
            message = cause.getMessage();
        }

        int statusCode = HttpServletResponse.SC_INTERNAL_SERVER_ERROR;

        if (exception instanceof ArgumentIsNullException) {

            message = StaticConstants.ARGUMENT_IS_NULL_EXCEPTION_MESSAGE;
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
            statusCode = HttpServletResponse.SC_EXPECTATION_FAILED;
            message = StaticConstants.TOKEN_VALIDATION_EXCEPTION_MESSAGE;
        }

        if (exception instanceof UserNotFoundException) {
            statusCode = HttpServletResponse.SC_NOT_FOUND;
            message = StaticConstants.USER_NOT_FOUND_EXCEPTION_MESSAGE;
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

        log.error(message);

        return new ErrorDto(
                new Date().toString(),
                statusCode,
                message );
    }
}
