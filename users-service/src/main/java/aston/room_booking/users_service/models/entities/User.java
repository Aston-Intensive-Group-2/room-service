package aston.room_booking.users_service.models.entities;

import aston.room_booking.users_service.models.enums.UserRole;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 *
 * @version 1.0
 * @author 4ndr33w
 */
@Valid
@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@Table(name = "users")
@AllArgsConstructor
public class User implements UserDetails {

    @Id
    @Column(name = "id", columnDefinition = "bigserial")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Id пользователя", example = "1")
    // тип поля изменён с примитива на объект
    // чтобы устранить эксплойт:
    // при создании нового пользователя
    // была возможность обновить существующего
    // указав его Id
    private Long id;

    @Column(name = "user_name", columnDefinition = "text", nullable = false, unique = true)
    @NotBlank(message = "Поле 'userName' - пустое или имеет некорректное наименование")
    @Schema(description = "UserName пользователя", example = "Andr33w")
    private String userName;

    @Column(name = "email", columnDefinition = "text", nullable = false, unique = true)
    @NotBlank(message = "Поле 'email' - пустое или имеет некорректное наименование")
    @Email(message = "Некорректный формат email")
    @Schema(description = "Email пользователя", example = "Andr33w@example.com")
    private String email;

    @Column(name = "first_name", columnDefinition = "text")
    @Schema(description = "FirstName пользователя", example = "Andrew")
    private String firstName = null;

    @Column(name = "last_name", columnDefinition = "text")
    @Schema(description = "LastName пользователя", example = "McFlyev")
    private String lastName = null;

    @Column(name = "password", columnDefinition = "text", nullable = false)
    @NotBlank(message = "Поле 'password' - пустое или имеет некорректное наименование")
    @Size(min = 3, message = "Пароль должен содержать минимум 3 символа")
    @Schema(description = "пароль пользователя", example = "qwerty")
    private String password;

    @Column(name = "phone", columnDefinition = "text")
    private String phone = null;

    @Column(name = "user_role", columnDefinition = "integer")
    @Enumerated(EnumType.ORDINAL)
    @Schema(description = "Роль пользователя", example = "USER")
    private UserRole userRole = UserRole.USER;

    @Column(name = "image", columnDefinition = "bytea")
    @Schema(description = "изображение пользователя", example = "null")
    private byte[] image = null;

    @Column(name = "created_at", columnDefinition = "timestamptz")
    private Date createdAt = new Date();

    @Column(name = "updated_at", columnDefinition = "timestamptz")
    private Date updatedAt = new Date();

    @Column(name = "last_login_date", columnDefinition = "timestamptz")
    private Date lastLoginDate = new Date();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(userRole);
    }

    @Override
    public String getUsername() {
        return userName;
    }
}