package aston.room_booking.users_service.configurations.security;

import aston.room_booking.users_service.configurations.security.filters.ExceptionHndlerFilter;
import aston.room_booking.users_service.configurations.security.filters.LoginAuthenticationFilter;
import aston.room_booking.users_service.configurations.security.filters.JwtAuthenticationFilter;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import static org.springframework.security.config.Customizer.withDefaults;

/**
 *
 * @version 1.0
 * @author 4ndr33w
 */
@Configuration
@EnableWebSecurity
@AllArgsConstructor
@EnableMethodSecurity
public class SecurityFilterChainConfig implements WebMvcConfigurer {

    private final LoginAuthenticationFilter loginAuthenticationFilter;
    private final JwtAuthenticationFilter jwtAuthFilter;

    private final ExceptionHndlerFilter exceptionHndlerFilter;

    /**
     * Фильтр безопасности; Определяет фильтрацию маршрутов: определяет к каким
     * эндпойнтам для каких типов запросов будет свободный доступ без аутенфикации;
     * <br/>
     *
     * @param httpSecurity
     * @return {@code SecurityFilterChain}
     * @throws Exception
     */
    @Bean
    public SecurityFilterChain filterChain (HttpSecurity httpSecurity) throws Exception {

        httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .cors(withDefaults())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.POST, "/api/v1/users").permitAll()
                        .requestMatchers(HttpMethod.POST,"/account/login").permitAll()
                        .requestMatchers("/swagger-ui/*").permitAll()
                        .requestMatchers("/v3/api-docs/**").permitAll()
                        .anyRequest().authenticated())
                .addFilterBefore(exceptionHndlerFilter, LoginAuthenticationFilter.class)
                .addFilter(loginAuthenticationFilter)
                .addFilterAfter(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)

                .httpBasic(withDefaults())
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return httpSecurity.build();
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("/*")
                .allowedHeaders("*")
                .allowCredentials(true);
    }
}