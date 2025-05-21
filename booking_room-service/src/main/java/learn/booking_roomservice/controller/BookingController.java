package learn.booking_roomservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import learn.booking_roomservice.clients.UserServerProxy;
import learn.booking_roomservice.dto.BookingDTO;
import learn.booking_roomservice.dto.BookingWithUserDTO;
import learn.booking_roomservice.dto.CancelRequestDTO;
import learn.booking_roomservice.dto.UserDTO;
import learn.booking_roomservice.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Бронирование", description = "Операции для управления бронированиями")
@RestController
@RequestMapping("/booking")
@RequiredArgsConstructor
public class BookingController {
    private final BookingService bookingService;
    private final UserServerProxy userServerProxy;

    @Operation(
            summary = "Получить все бронирования пользователя",
            description = "Возвращает список бронирований пользователя"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешно получены бронирования"),
            @ApiResponse(responseCode = "204", description = "Бронирования не найдены")
    })
    @GetMapping("/all")
    public ResponseEntity<List<BookingWithUserDTO>> getAll(@RequestHeader("Authorization") String authHeader) {
        UserDTO user = userServerProxy.get(authHeader).getBody();
        List<BookingWithUserDTO> bookings = bookingService.getAll(user.id())
                .stream()
                .map(b -> new BookingWithUserDTO(b.id(), user, b.roomId(), b.start(), b.end(), b.status(), b.createdAt()))
                .toList();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(bookings);
    }

    @Operation(
            summary = "Создание бронирования",
            description = "Создает бронирование для пользователя"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Бронирование успешно создано"),
            @ApiResponse(responseCode = "400", description = "Время бронирования уже занято")
    })
    @PostMapping("/create")
    public ResponseEntity<BookingDTO> createBooking(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody @Valid BookingDTO bookingDTO) {
        UserDTO user = userServerProxy.get(authHeader).getBody();
        BookingDTO bookingDTO1 = new BookingDTO(user.id(), bookingDTO.roomId(), bookingDTO.start(), bookingDTO.end());
        bookingService.addBooking(bookingDTO1);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(bookingDTO1);
    }

    @Operation(
            summary = "Отмена бронирования",
            description = "Отменяет активное бронирование пользователя"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Бронирование успешно отменено"),
            @ApiResponse(responseCode = "404", description = "Активное бронирование не найдено")
    })
    @PatchMapping("/cancelled")
    public ResponseEntity<BookingDTO> cancelledBooking(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody @Valid CancelRequestDTO cancelRequestDTO
            ) {
        UserDTO user = userServerProxy.get(authHeader).getBody();
        BookingDTO bookingDTO = bookingService.cancelledBooking(cancelRequestDTO.bookingId(), user.id());
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(bookingDTO);
    }
}
