package aston.room_booking.users_service.configurations.security.components;

import aston.room_booking.users_service.repositories.interfaces.UserRepository;
import aston.room_booking.users_service.utils.StaticConstants;
import aston.room_booking.users_service.utils.TestUtils;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.nio.charset.StandardCharsets;

/**
 * @author 4ndr33w
 * @version 1.0
 *//*
public class JwtTokenProvider extends TestUtils {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private JwtTokenProvider tokenProvider;

    private final Algorithm algorithm = Algorithm.HMAC256(StaticConstants.JWT_SECRET.getBytes(StandardCharsets.UTF_8));

    private JWTVerifier verifier = JWT.require(algorithm)
            .withIssuer(StaticConstants.JWT_ISSUER)
            .build();

    @Test
    public void createTokenTest() {


    }
}*/
