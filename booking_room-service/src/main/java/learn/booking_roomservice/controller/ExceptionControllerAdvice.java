package learn.booking_roomservice.controller;

import learn.booking_roomservice.exception.BookingNotFoundException;
import learn.booking_roomservice.exception.TimeBookingRegistrationException;
import learn.booking_roomservice.exception.model.ErrorDetails;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice()
public class ExceptionControllerAdvice {
    @ExceptionHandler({TimeBookingRegistrationException.class})
    ResponseEntity<ErrorDetails> exceptionTimeBookingRegistration() {
        ErrorDetails error = new ErrorDetails("Время регистрации уже занято!");
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(error);

    }

    @ExceptionHandler(BookingNotFoundException.class)
    ResponseEntity<ErrorDetails> exceptionBookingNotFound() {
        ErrorDetails error = new ErrorDetails("Активная бронь не найдена!");
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(error);
    }
}
