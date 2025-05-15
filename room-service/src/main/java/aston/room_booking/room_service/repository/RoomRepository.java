package aston.room_booking.room_service.repository;

import aston.room_booking.room_service.model.entity.RoomEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomRepository extends JpaRepository<RoomEntity, Long> {
}
