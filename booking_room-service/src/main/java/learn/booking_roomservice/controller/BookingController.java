package learn.booking_roomservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import learn.booking_roomservice.clients.RoomServiceProxy;
import learn.booking_roomservice.clients.UserServiceProxy;
import learn.booking_roomservice.dto.BookingDTO;
import learn.booking_roomservice.dto.BookingWithUserAndRoomDTO;
import learn.booking_roomservice.dto.CancelRequestDTO;
import learn.booking_roomservice.dto.UserDTO;
import learn.booking_roomservice.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Tag(name = "Бронирование", description = "Операции для управления бронированиями")
@RestController
@RequestMapping("/booking")
@RequiredArgsConstructor
public class BookingController {
    private final BookingService bookingService;
    private final UserServiceProxy userServerProxy;
    private final RoomServiceProxy roomServiceProxy;

    @Operation(
            summary = "Получить все бронирования пользователя",
            description = "Возвращает список бронирований пользователя"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешно получены бронирования"),
            @ApiResponse(responseCode = "204", description = "Бронирования не найдены")
    })
    @GetMapping("/all")
    public ResponseEntity<List<BookingWithUserAndRoomDTO>> getAllBookingsUser(@RequestHeader("Authorization") String authHeader) {
        UserDTO user = userServerProxy.get(authHeader).getBody();
        List<BookingWithUserAndRoomDTO> bookings = bookingService.getAllBookingsByUserId(user.id())
                .stream()
                .map(b -> new BookingWithUserAndRoomDTO(b.id(), user, roomServiceProxy.getRoom(b.roomId()).getBody(), b.start(), b.end(), b.status(), b.createdAt()))
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
    public ResponseEntity<BookingDTO> cancelledBookingUserByBookingId(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody @Valid CancelRequestDTO cancelRequestDTO
            ) {
        UserDTO user = userServerProxy.get(authHeader).getBody();
        BookingDTO bookingDTO = bookingService.cancelledBookingByBookingId(cancelRequestDTO.bookingId(), user.id());
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(bookingDTO);
    }

    @Operation(
            summary = "Получить бронирование по ID",
            description = "Возвращает бронирование по идентификатору"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Бронирование найдено"),
            @ApiResponse(responseCode = "404", description = "Бронирование не найдено")
    })
    @GetMapping("/{id}")
    public ResponseEntity<BookingDTO> getBookingById(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable UUID id) {
        UserDTO user = userServerProxy.get(authHeader).getBody();
        BookingDTO bookingDTO = bookingService.getBookingById(user.id(), id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(bookingDTO);
    }

    @Operation(
            summary = "Получить все бронирования пользователя по ID",
            description = "Возвращает список всех бронирований пользователя по ID"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Бронирования найдены"),
            @ApiResponse(responseCode = "204", description = "Бронирования не найдены")
    })
    @GetMapping("/all/{userId}")
    public ResponseEntity<List<BookingDTO>> getAllBookings(@PathVariable Long userId) {
        List<BookingDTO> bookings = bookingService.getAllBookingsByUserId(userId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(bookings);
    }
}
