package learn.booking_roomservice.service;

import learn.booking_roomservice.model.Booking;

import java.util.List;

public interface BookingService {
    void addBooking(Booking booking);
    List<Booking> getAll();
}
