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
@Schema(description = "Модель пользователя")
public class User implements UserDetails {

    @Id
    @Column(name = "id", columnDefinition = "bigserial")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Id пользователя; генерируется на стороне БД", example = "1", nullable = true)
    // тип поля изменён с примитива на объект
    // чтобы устранить эксплойт:
    // при создании нового пользователя
    // была возможность обновить существующего
    // указав его Id
    private Long id;

    @Column(name = "user_name", columnDefinition = "text", nullable = false, unique = true)
    @NotBlank(message = "Поле 'userName' - пустое или имеет некорректное наименование")
    @Schema(description = "UserName пользователя. Уникальное значение", example = "Andr33w", nullable = false)
    private String userName;

    @Column(name = "email", columnDefinition = "text", nullable = false, unique = true)
    @NotBlank(message = "Поле 'email' - пустое или имеет некорректное наименование")
    @Email(message = "Некорректный формат email")
    @Schema(description = "Email пользователя. Уникальное значение", example = "Andr33w@example.com")
    private String email;

    @Column(name = "first_name", columnDefinition = "text")
    @Schema(description = "FirstName пользователя; допускается null", example = "Andrew", nullable = true)
    private String firstName = null;

    @Column(name = "last_name", columnDefinition = "text")
    @Schema(description = "LastName пользователя; допускается null", example = "McFlyev", nullable = true)
    private String lastName = null;

    @Column(name = "password", columnDefinition = "text", nullable = false)
    @NotBlank(message = "Поле 'password' - пустое или имеет некорректное наименование")
    @Size(min = 3, message = "Пароль должен содержать минимум 3 символа")
    @Schema(description = "пароль пользователя. Должен быть не пустым; минимум 3 мимвола", example = "qwerty")
    private String password;

    @Column(name = "phone", columnDefinition = "text")
    @Schema(description = "Телефонный номер пользователя; допускается null", example = "+1234567890", nullable = true)
    private String phone = null;

    @Column(name = "user_role", columnDefinition = "integer")
    @Enumerated(EnumType.ORDINAL)
    @Schema(description = "Роль пользователя. По умлолчанию в сервисе ставится USER. Администратор (/api/v1/admin) может задавать любую роль", example = "USER", implementation = UserRole.class)
    private UserRole userRole = UserRole.USER;

    @Column(name = "image", columnDefinition = "bytea")
    @Schema(description = "изображение пользователя; допускается null", example = "null", nullable = true)
    private byte[] image = null;

    @Column(name = "created_at", columnDefinition = "timestamptz")
    @Schema(description = "timestamptz создания. Задаётся на стороне сервера", nullable = true)
    private Date createdAt = new Date();

    @Column(name = "updated_at", columnDefinition = "timestamptz")
    @Schema(description = "timestamptz обновления. Задаётся на стороне сервера", nullable = true)
    private Date updatedAt = new Date();

    @Column(name = "last_login_date", columnDefinition = "timestamptz")
    @Schema(description = "Временной штамп последней активности. Задаётся на стороне сервера", nullable = true)
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