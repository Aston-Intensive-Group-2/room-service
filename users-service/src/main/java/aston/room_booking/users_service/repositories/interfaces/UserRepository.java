package aston.room_booking.users_service.repositories.interfaces;

import aston.room_booking.users_service.models.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 *
 * @version 1.0
 * @author 4ndr33w
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    Optional<User> findByUserName(String email);
}
