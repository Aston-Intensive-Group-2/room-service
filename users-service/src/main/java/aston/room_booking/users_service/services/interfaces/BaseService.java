package aston.room_booking.users_service.services.interfaces;

import java.util.Collection;

public interface BaseService<D, E> {

    D create(E entity);
    Collection<D> getAll();
    D getByEmail(String email);
    D getById(long id);
    D getByUserName(String userName);
    boolean deleteById(long id);
    D update(long id, D dto);
}
