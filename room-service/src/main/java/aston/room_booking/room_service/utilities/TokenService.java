package aston.room_booking.room_service.utilities;

import java.util.List;

public interface TokenService {
    boolean isTokenValid(String token);
    List<String> getUserRoles(String token);
}
