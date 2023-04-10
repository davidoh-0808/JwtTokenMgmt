package test.yezac2.global.config.springDoc;


import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;


@Configuration
public class OpenAPIConfig {

    @Value("${yezac.openapi.dev-url}")
    private String devUrl;

    @Value("${yezac.openapi.prod-url}")
    private String prodUrl;

    @Bean
    public OpenAPI myOpenAPI() {
        Server devServer = new Server();
        devServer.setUrl( devUrl );
        devServer.setDescription("개발계 OpenAPI");

        Server prodServer = new Server();
        prodServer.setUrl( prodUrl );
        prodServer.setDescription("운영계 OpenAPI");

        Contact contact = new Contact();
        contact.setEmail("yezac-team@m-ultiply.com");
        contact.setName("예작팀");
        contact.setUrl("https://docs.m-ultiply.com/auth");

        License mitLicense = new License().name("MIT License").url("https://choosealicense.com/licenses/mit/");

        Info info = new Info()
                .title("Tutorial Management API")
                .version("1.0")
                .contact(contact)
                .description("예작팀 화이팅..").termsOfService("https://docs.m-ultiply.com/auth")
                .license(mitLicense);

        return new OpenAPI().info(info).servers(List.of(devServer, prodServer));
    }
}
