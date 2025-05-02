package aston.room_booking.users_service.models.enums;

import org.springframework.security.core.GrantedAuthority;

/**
 *
 * @version 1.0
 * @author 4ndr33w
 */
public enum UserRole implements GrantedAuthority {
    USER,
    ADMIN;

    @Override
    public String getAuthority() {
        return this.name();
    }
}
