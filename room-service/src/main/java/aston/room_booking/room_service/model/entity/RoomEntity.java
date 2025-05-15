package aston.room_booking.room_service.model.entity;

import aston.room_booking.room_service.model.RoomStatus;
import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "rooms")
public class RoomEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "room_id")
    private Long roomId;
    @Column(name = "room_width")
    private Integer roomWidth;
    @Column(name = "room_length")
    private Integer roomLength;
    @Column(name = "room_height")
    private Integer roomHeight;
    @Column(name = "room_address")
    private String roomAddress;
    @Enumerated(EnumType.STRING)
    @Column(name = "room_status")
    private RoomStatus roomStatus;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "roomEntity")
    private List<EquipmentEntity> equipmentEntityList;
}
