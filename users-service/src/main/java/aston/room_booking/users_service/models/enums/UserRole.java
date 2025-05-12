package aston.room_booking.users_service.models.enums;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.security.core.GrantedAuthority;

/**
 *
 * @version 1.0
 * @author 4ndr33w
 */
@Schema(
        description = "Роли пользователя; допускается числовые значения (0; 1)",
        allowableValues = {"0", "1"},
        defaultValue = "0",
        example = "USER"
)
public enum UserRole implements GrantedAuthority {
    @Schema(description = "Обычный пользователь (числовой код: 0)")
    USER(0),
    @Schema(description = "Администратор (числовой код: 1)")
    ADMIN(1);

    private final int code;

    UserRole(int code) {
        this.code = code;
    }

    @Override
    public String getAuthority() {
        return this.name();
    }
}
