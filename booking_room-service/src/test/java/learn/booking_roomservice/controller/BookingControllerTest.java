package learn.booking_roomservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import learn.booking_roomservice.clients.UserServerProxy;
import learn.booking_roomservice.common.Status;
import learn.booking_roomservice.dto.BookingDTO;
import learn.booking_roomservice.dto.CancelRequestDTO;
import learn.booking_roomservice.dto.UserDTO;
import learn.booking_roomservice.exception.BookingNotFoundException;
import learn.booking_roomservice.exception.BookingsUserNotFoundException;
import learn.booking_roomservice.exception.TimeBookingRegistrationException;
import learn.booking_roomservice.repository.BookingRepository;
import learn.booking_roomservice.service.BookingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/*
	•	given — настройка mock’ов
	•	when — вызов mockMvc
	•	then — проверка результата
	•	verify — что mock’и вызваны

 */
@AutoConfigureMockMvc
@WebMvcTest(BookingController.class)
class BookingControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private BookingService bookingService;
    @MockitoBean
    private UserServerProxy userServerProxy;
    @MockitoBean
    private BookingRepository bookingRepository;

    private BookingDTO bookingDTO;
    private UserDTO userDTO;
    private String token;

    @BeforeEach
    void setUp() {
        objectMapper.registerModule(new JavaTimeModule());
        token = "Bearer test-token";

        userDTO = new UserDTO(
                1L,
                "userName",
                "ivan@mail.ru",
                "firstName",
                "lastName",
                "123456");

        bookingDTO = new BookingDTO(userDTO.id(), 2L,
                LocalDateTime.of(2025, 5, 15, 13, 30),
                LocalDateTime.of(2025, 5, 15, 15, 30));
    }

    @Test
    void createBooking_shouldReturnCreatedBooking() throws Exception {
        // given
        given(userServerProxy.get(token)).willReturn(ResponseEntity.ok(userDTO));

        // when/then
        mockMvc.perform(post("/booking/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", token)
                        .content(objectMapper.writeValueAsString(bookingDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.userId").value(userDTO.id()))
                .andExpect(jsonPath("$.roomId").value(2));

        // verify
        verify(userServerProxy).get(token);
        verify(bookingService).addBooking(any(BookingDTO.class));
    }

    @Test
    void createBooking_shouldException_whenCollisionTime() throws Exception {
        // given
        given(userServerProxy.get(token)).willReturn(ResponseEntity.ok(userDTO));
        given(bookingService.addBooking(any(BookingDTO.class))).willThrow(TimeBookingRegistrationException.class);

        // when/then
        mockMvc.perform(post("/booking/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", token)
                        .content(objectMapper.writeValueAsString(bookingDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Время регистрации уже занято!"));

        // verify
        verify(bookingService).addBooking(any(BookingDTO.class));
        verify(userServerProxy).get(token);
    }

    @Test
    void getAll_shouldReturnListOfBookingsWithUser() throws Exception {
        // given
        List<BookingDTO> bookings = List.of(bookingDTO);
        given(userServerProxy.get(token)).willReturn(ResponseEntity.ok(userDTO));
        given(bookingService.getAllBookingsByUserId(userDTO.id())).willReturn(bookings);

        // when/then
        mockMvc.perform(get("/booking/all")
                        .header("Authorization", token)
                ).andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].user.userName").value(userDTO.userName()))
                .andExpect(jsonPath("$[0].roomId").value(2));

        // verify
        verify(bookingService, times(1)).getAllBookingsByUserId(userDTO.id());
        verify(userServerProxy).get(token);
    }

    @Test
    void getAll_shouldReturnException_whenBookingsNotFound() throws Exception {
        // given
        given(userServerProxy.get(token)).willReturn(ResponseEntity.ok(userDTO));
        given(bookingService.getAllBookingsByUserId(userDTO.id())).willThrow(BookingsUserNotFoundException.class);

        // when/then
        mockMvc.perform(get("/booking/all")
                        .header("Authorization", token))
                .andExpect(status().isNoContent());
    }

    @Test
    void cancelledBooking_shouldReturnBookingDTO_whenBookingActiveExist() throws Exception {
        // given
        UUID bookingId = UUID.randomUUID();
        CancelRequestDTO requestDTO = new CancelRequestDTO(bookingId);
        BookingDTO cancelledBooking = new BookingDTO(bookingId, userDTO.id(), 3L,
                LocalDateTime.of(2025, 5, 15, 13, 30),
                LocalDateTime.of(2025, 5, 15, 15, 30),
                Status.CANCELLED, LocalDateTime.now());
        given(userServerProxy.get(token)).willReturn(ResponseEntity.ok(userDTO));
        given(bookingService.cancelledBookingByBookingId(bookingId, userDTO.id())).willReturn(cancelledBooking);

        // when/then
        mockMvc.perform(patch("/booking/cancelled")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", token)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("CANCELLED"))
                .andExpect(jsonPath("$.roomId").value(3))
                .andExpect(jsonPath("$.userId").value(userDTO.id()));


        // verify
        verify(userServerProxy, times(1)).get(token);
        verify(bookingService, times(1)).cancelledBookingByBookingId(requestDTO.bookingId(), userDTO.id());
    }

    @Test
    void cancelledBooking_shouldReturnException_whenBookingsNotFound() throws Exception {
        // given
        UUID bookingId = UUID.randomUUID();
        CancelRequestDTO requestDTO = new CancelRequestDTO(bookingId);
        given(userServerProxy.get(token)).willReturn(ResponseEntity.ok(userDTO));
        given(bookingService.cancelledBookingByBookingId(bookingId, userDTO.id())).willThrow(BookingNotFoundException.class);

        // when/then
        mockMvc.perform(patch("/booking/cancelled")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", token)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Активная бронь не найдена!"));

        // verify
        verify(userServerProxy, times(1)).get(token);
        verify(bookingService, times(1)).cancelledBookingByBookingId(requestDTO.bookingId(), userDTO.id());
    }
}
