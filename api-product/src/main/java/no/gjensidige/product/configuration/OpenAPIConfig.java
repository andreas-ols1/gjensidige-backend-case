package no.gjensidige.product.configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * OpenAPI 3.0 configuration for automatic API documentation generation
 * Replaces the legacy Swagger 2 configuration
 */
@Configuration
public class OpenAPIConfig {

        @Bean
        public OpenAPI productAPI() {
                Server devServer = new Server();
                devServer.setUrl("http://localhost:8080");
                devServer.setDescription("Development server");

                Contact contact = new Contact();
                contact.setEmail("support@gjensidige.no");
                contact.setName("Gjensidige API Team");
                contact.setUrl("https://www.gjensidige.no");

                License mitLicense = new License()
                                .name("MIT License")
                                .url("https://choosealicense.com/licenses/mit/");

                Info info = new Info()
                                .title("Product Management API")
                                .version("1.0.0")
                                .contact(contact)
                                .description("REST API for managing products in the Gjensidige system")
                                .license(mitLicense);

                return new OpenAPI()
                                .info(info)
                                .servers(List.of(devServer));
        }
}