package aston.room_booking.users_service.configurations.security;

import aston.room_booking.users_service.configurations.security.components.JwtTokenProvider;
import aston.room_booking.users_service.configurations.security.filters.ExceptionHndlerFilter;
import aston.room_booking.users_service.configurations.security.filters.LoginAuthenticationFilter;
import aston.room_booking.users_service.configurations.security.filters.JwtAuthenticationFilter;
import aston.room_booking.users_service.repositories.interfaces.UserRepository;
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
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
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

    /**
     * Конфигурирование бина фильтра входа по логину и паролю
     * <br/>
     * Удалось избавиться от явного введения конструктора в фильтре
     * <br/>
     * Однако конструктор в любом случае пришлось явно объявлять конструктор если не в классе,
     * то здесь - в ручном конфигурировании бина
     *
     * @param userRepository Реозиторий пользователей
     * @param jwtTokenProvider Провайдер токенов
     * @param authenticationManager Менеджер аутенфикации
     * @return {@code LoginAuthenticationFilter} - сконфигурированный вручную бин фильтра
     */
    @Bean
    public LoginAuthenticationFilter loginAuthenticationFilter(
            UserRepository userRepository,
            JwtTokenProvider jwtTokenProvider,
            CustomAuthenticationManager authenticationManager) {

        LoginAuthenticationFilter filter = new LoginAuthenticationFilter(
                userRepository,
                authenticationManager,
                jwtTokenProvider
        );
        filter.setAuthenticationManager(authenticationManager);
        filter.setFilterProcessesUrl("/account/login");
        return filter;
    }

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
    public SecurityFilterChain filterChain (HttpSecurity httpSecurity,
                                            LoginAuthenticationFilter loginFilter,
                                            JwtAuthenticationFilter jwtFilter,
                                            ExceptionHndlerFilter exceptionFilter) throws Exception {
        httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .cors(withDefaults())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.POST, "/api/v1/users").permitAll()
                        .requestMatchers(HttpMethod.GET, "/swagger-ui/**").permitAll()
                        .requestMatchers(HttpMethod.POST,"/account/login").permitAll()
                        .requestMatchers("/api/v1/users/v3/api-docs/**").permitAll()
                        .requestMatchers("/api/v1/users/swagger-ui/**").permitAll()
                        .requestMatchers("/v3/api-docs/**").permitAll()
                        .anyRequest().authenticated())
                .addFilterBefore(exceptionFilter, LoginAuthenticationFilter.class)
                .addFilter(loginFilter)
                .addFilterAt(jwtFilter, BasicAuthenticationFilter.class)
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