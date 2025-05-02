package aston.room_booking.users_service.models.entities;

import aston.room_booking.users_service.models.enums.UserRole;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User {

    @Id
    @Column(name = "id", columnDefinition = "bigserial")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "user_name", columnDefinition = "text", nullable = false, unique = true)
    private String userName;

    @Column(name = "email", columnDefinition = "text", nullable = false, unique = true)
    private String email;

    @Column(name = "first_name", columnDefinition = "text")
    @Builder.Default
    private String firstName = null;

    @Column(name = "last_name", columnDefinition = "text")
    @Builder.Default
    private String lastName = null;

    @Column(name = "password", columnDefinition = "text", nullable = false)
    private String password;

    @Column(name = "phone", columnDefinition = "text")
    @Builder.Default
    private String phone = null;

    @Column(name = "user_role", columnDefinition = "integer")
    @Enumerated(EnumType.ORDINAL)
    @Builder.Default
    private UserRole userRole = UserRole.USER;

    @Column(name = "image", columnDefinition = "bytea")
    @Builder.Default
    private byte[] image = null;

    @Column(name = "created_at", columnDefinition = "timestamptz")
    @Builder.Default
    private Date createdAt = new Date();

    @Column(name = "updated_at", columnDefinition = "timestamptz")
    @Builder.Default
    private Date updatedAt = new Date();

    @Column(name = "last_login_date", columnDefinition = "timestamptz")
    @Builder.Default
    private Date lastLoginDate = new Date();
}