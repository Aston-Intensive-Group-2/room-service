package aston.room_booking.users_service.configurations.security.components;

import aston.room_booking.users_service.models.entities.User;
import aston.room_booking.users_service.utils.StaticConstants;
import aston.room_booking.users_service.utils.exceptions.AuthorizationException;
import aston.room_booking.users_service.utils.exceptions.JwtException;
import aston.room_booking.users_service.repositories.interfaces.UserRepository;
import aston.room_booking.users_service.utils.exceptions.TokenValidationException;

import aston.room_booking.users_service.utils.exceptions.UserNotFoundException;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Класс-провайдер токенов
 * <p>
 *     Отвечает за:
 * <ul>
 *   <li>Создание нового токена</li>
 *   <li>Извлечение токена из запроса</li>
 *   <li>Валидацию токена</li>
 *   <li>Извлечение {@code email} из токена</li>
 * </ul>
 * </p>
 *
 * @version 1.0
 * @author 4ndr33w
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

    private final UserRepository userRepository;

    private Algorithm algorithm = Algorithm.HMAC256(StaticConstants.JWT_SECRET.getBytes(StandardCharsets.UTF_8));

    private JWTVerifier verifier = JWT.require(algorithm)
            .withIssuer(StaticConstants.JWT_ISSUER)
            .build();

    /**
     * Метод создания токена из объекта модели пользователя
     *
     * @param user модель объекта, {@code entity} профиля пользователя
     * @return {@code token} - JsonWebToken для Basic Auth
     */
    public String createToken(User user) {

        return JWT.create()
                .withSubject(user.getEmail())
                .withClaim("roles", user.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.toList()))
                .withJWTId(String.valueOf(user.getId()))
                .withIssuedAt(new Date())
                .withIssuer(StaticConstants.JWT_ISSUER)
                .withExpiresAt(new Date(System.currentTimeMillis() + StaticConstants.JWT_EXPIRATION_MS))
                .sign(algorithm);
    }

    private DecodedJWT getDecodedToken(String token) throws JwtException {
        try {
            return verifier.verify(token);
        }
        catch (JWTVerificationException e) {
            log.error("%s; %s".formatted(StaticConstants.JWT_VERIFICATION_EXCEPTION, e.getMessage()));

            throw new JwtException(StaticConstants.JWT_VERIFICATION_EXCEPTION);
        }
    }

    /**
     * Метод возвращает {@code email} из переданного в качестве параметра JWT: {@code token}
     *
     * @param token JsonWebToken для Basic Auth
     * @return {@code email} - email пользователя
     *
     * @throws TokenValidationException ошибка декодирования токена
     */
    public String getUserNameFromToken(String token) throws TokenValidationException {
        try {
            var decodedToken = getDecodedToken(token);

            return decodedToken.getSubject();
        }
        catch (Exception e) {
            log.error("%s; %s".formatted(StaticConstants.TOKEN_VALIDATION_EXCEPTION_MESSAGE, e.getMessage()));
            throw new TokenValidationException(StaticConstants.TOKEN_VALIDATION_EXCEPTION_MESSAGE, e);
        }
    }

    /**
     * Метод осуществляет валидацию токена
     * <p>
     *     метод:
     * <ul>
     *   <li>извлекает {@code email} из токена</li>
     *   <li>проверяет срок истечения действия токена</li>
     * </ul>
     * </p>
     *
     * @param token JsonWebToken для Basic Auth
     * @return boolean - {@code true} в случае, если токен валиден
     *
     * @throws TokenValidationException в случае, если токен не валиден
     */
    public boolean validateToken(String token) throws TokenValidationException {
        try {
            final String userName = getUserNameFromToken(token);

            return (userName != null && !isTokenExpired(token));
        } catch (JWTVerificationException exception) {

            log.error(StaticConstants.TOKEN_VALIDATION_EXCEPTION_MESSAGE);
            throw new TokenValidationException(StaticConstants.TOKEN_VALIDATION_EXCEPTION_MESSAGE);
        }
    }

    /**
     * Метод осуществляет валидацию токена; в случае успешной валидации возвращает объект {@code Optional<User>}
     * <p>
     *     метод:
     * <ul>
     *   <li>извлекает {@code email} из токена</li>
     *   <li>проверяет,существует ли пользователь с таким {@code email}</li>
     *   <li>проверяет срок истечения действия токена</li>
     *   <li>в случае успешной валидации возвращает {@code Optional<User>}</li>
     * </ul>
     * </p>
     *
     * @param token JsonWebToken для Basic Auth
     * @return {@code Optional<User>} - в случае, если токен валиден
     *
     * @throws TokenValidationException в случае, если срок действия токена истёк
     */
    public Optional<User> validateTokenAndReturnOptionalOfUser(String token) throws TokenValidationException, UserNotFoundException {

        try {
            var isTokenValid = validateToken(token);

            if(isTokenValid) {
                var email = getUserNameFromToken(token);
                return userRepository.findByEmail(email);
            }
        }
        catch (Exception e) {
            log.error(StaticConstants.TOKEN_VALIDATION_EXCEPTION_MESSAGE);
            throw new TokenValidationException(StaticConstants.TOKEN_VALIDATION_EXCEPTION_MESSAGE);
        }
        throw new TokenValidationException(StaticConstants.TOKEN_VALIDATION_EXCEPTION_MESSAGE);
    }

    /**
     * Метод извлекает {@code token} из запроса {@code HttpServletRequest}
     *
     * @param request
     * @return String {@code token} - JsonWebToken для Basic Auth
     */
    public String resolveToken (HttpServletRequest request) {
        if(request.getHeader("Authorization") == null) {
            throw new AuthorizationException(StaticConstants.BEARER_AUTHORIZATION_HEADER_IS_MISSING_EXCEPTION_MESSAGE);
        }
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    private boolean isTokenExpired(String token) {
        var decodedToken = getDecodedToken(token);
        final Date expiration =  decodedToken.getExpiresAt();

        return expiration.before(new Date());
    }
}
