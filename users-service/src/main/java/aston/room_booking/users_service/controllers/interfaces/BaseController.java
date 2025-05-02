package aston.room_booking.users_service.controllers.interfaces;

import org.springframework.http.ResponseEntity;

public interface BaseController<T, E> {

    ResponseEntity<?> getById(long id);

    boolean deletById (long id);
    T updateById(long id, T item);
    ResponseEntity<?> create(E entity);

}
