package aston.room_booking.notification_service.model;


import aston.room_booking.notification_service.enums.BookingStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class Notification {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private Long userId;
  private Long roomId;
  private String userName;
  private String email;
  private String phone;
  private LocalDateTime start;
  private LocalDateTime end;
  @Enumerated(EnumType.STRING)
  private BookingStatus status;
  @Column(columnDefinition = "TEXT")
  private String message;

}
