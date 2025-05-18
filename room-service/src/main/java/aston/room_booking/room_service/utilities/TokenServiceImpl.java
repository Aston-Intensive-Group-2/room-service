package aston.room_booking.room_service.utilities;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class TokenServiceImpl implements TokenService {
    private final String jwtSecretKey = "yourSecretKeyShouldBeLongerAndMoreComplexdwfFV_we_f34_F_F#ER#WDDWr3tr_";
    private final String jwtIssuer = "Andr33w";
    private final Algorithm algorithm = Algorithm.HMAC256(jwtSecretKey);

    private DecodedJWT decodeJwt(String token) {
        JWTVerifier verifier = JWT.require(algorithm).build();
        return verifier.verify(token);
    }

    @Override
    public boolean isTokenValid(String token) {
        return this.decodeJwt(token).getIssuer().equals(jwtIssuer);
    }

    @Override
    public List<String> getUserRoles(String token) {
        return this.decodeJwt(token).getClaim("roles").asList(String.class);
    }
}
