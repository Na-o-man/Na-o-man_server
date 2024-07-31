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

        Server server = new Server();
        server.setUrl("https://www.naoman.site");

        Server local = new Server();
        local.setUrl("http://localhost:8080");

        return new OpenAPI()
                .components(new Components().addSecuritySchemes("bearerAuth", securityScheme))
                .security(Arrays.asList(securityRequirement))
                .info(apiInfo()).servers(List.of(server, local));
    }

    private Info apiInfo() {
        return new Info()
                .title("나ㅇ만 서비스 API")
                .description("친구, 가족, 동료와 찍은 사진들을 손쉽게 공유할 수 있도록 돕는 서비스, 나ㅇ만입니다.")
                .version("1.0.0");
    }
}
