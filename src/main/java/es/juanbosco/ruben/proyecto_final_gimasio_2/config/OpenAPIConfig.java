package es.juanbosco.ruben.proyecto_final_gimasio_2.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenAPIConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API REST - Sistema de Gestión de Gimnasio")
                        .version("1.0.0")
                        .description("API REST completa para la gestión integral de un gimnasio. " +
                                "Incluye gestión de socios, clases, entrenadores, reservas, bonos, planes y horarios.")
                        .contact(new Contact()
                                .name("Rubén")
                                .email("contacto@gimnasio.com")))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:8080")
                                .description("Servidor de desarrollo")
                ));
    }
}
