package aston.room_booking.users_service.configurations.openapi;

import aston.room_booking.users_service.models.enums.UserRole;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.links.Link;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author 4ndr33w
 * @version 1.0
 */
@Configuration
@AllArgsConstructor
public class OpenApiConfiguration {

    private final Environment environment;

    @Bean
    public OpenAPI defineOpenAPI () {

        Server roomServer = new Server();
        String roomsServerUrl = environment.getProperty("rooms.server.url");

        Server userServer = new Server();
        String userServerUrl = environment.getProperty("users.server.url");
        userServer.setUrl(userServerUrl);
        userServer.setDescription("Users API Server");
        userServer.addExtension("x-link", java.util.Map.of(
                "roomsApi", new Link()
                        .operationId("getRooms")
                        .server(new Server().url(roomsServerUrl))
        ));

        roomServer.setUrl(roomsServerUrl);
        roomServer.setDescription("Rooms API Server");
        roomServer.addExtension("x-link", java.util.Map.of(
                "userApi", new Link()
                        .operationId("getUser")
                        .server(new Server().url(userServerUrl))
        ));

        Contact myContact = new Contact();
        myContact.setName("Andr33w McFly");
        myContact.setEmail("my.email@example.com");

        SecurityScheme jwtScheme = new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT")
                .name("Authorization")
                .in(SecurityScheme.In.HEADER)
                .description("Bearer Token");

        io.swagger.v3.oas.models.links.Link link = new io.swagger.v3.oas.models.links.Link();
        link.setOperationId("login");

        SecurityRequirement securityRequirement = new SecurityRequirement()
                .addList("bearerAuth");

        Info info = new Info()
                .title("API для управления пользователями сервиса")
                .version("1.0")
                .description("API предоставляет эндпоинты для управления пользователями сервиса.")
                .contact(myContact);

        return new OpenAPI()
                .info(info)
                .servers(List.of(userServer, roomServer))
                .components(new Components()
                        .addSecuritySchemes("bearerAuth", jwtScheme))
                .addSecurityItem(securityRequirement);
    }
}
