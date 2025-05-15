package aston.room_booking.room_service.repository;

import aston.room_booking.room_service.model.entity.EquipmentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EquipmentRepository extends JpaRepository<EquipmentEntity, Long> {
}
