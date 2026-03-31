package com.eseltech.appbackendatelie.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Configuração de CORS (Cross-Origin Resource Sharing) para a aplicação.
 * Permite requisições de origens explícitas para todos os endpoints da API.
 *
 * <p>Esta configuração é essencial para permitir que aplicações frontend
 * hospedadas em domínios diferentes possam acessar a API, especialmente
 * para trafegar cookies HttpOnly de autenticação.</p>
 *
 * @author EselTech
 * @version 2.0.0
 * @since 2024
 */
@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Value("${cors.allowed-origins:http://localhost:3000}")
    private String allowedOrigins;

    /**
     * Configura o mapeamento CORS para permitir requisições de origens específicas
     * com suporte a credentials (cookies HttpOnly).
     *
     * @param registry Registro de configurações CORS do Spring
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins(allowedOrigins.split(","))
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true);
    }
}
