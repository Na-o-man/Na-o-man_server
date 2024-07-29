package com.umc.naoman.global.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI openAPI() {
        SecurityScheme securityScheme = new SecurityScheme()
                .type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("JWT")
                .in(SecurityScheme.In.HEADER).name("Authorization");
        SecurityRequirement securityRequirement = new SecurityRequirement().addList("bearerAuth");

//        Server server = new Server();
//        server.setUrl("http://{EC2_인스턴스의_IPv4_주소}");

        Server local = new Server();
        local.setUrl("http://localhost:8080");

        return new OpenAPI()
                .components(new Components().addSecuritySchemes("bearerAuth", securityScheme))
                .security(Arrays.asList(securityRequirement))
                .info(apiInfo()).servers(List.of(local));
    }

    private Info apiInfo() {
        return new Info()
                .title("턴페이지 서비스 API")
                .description("독후감 작성, 조회와 중고 도서 판매 등의 기능을 제공합니다.")
                .version("1.0.0");
    }
}
