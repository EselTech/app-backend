package com.eseltech.appbackendatelie.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Configuração de CORS (Cross-Origin Resource Sharing) para a aplicação.
 * Permite requisições de diferentes origens para todos os endpoints da API.
 *
 * <p>Esta configuração é essencial para permitir que aplicações frontend
 * hospedadas em domínios diferentes possam acessar a API.</p>
 *
 * @author EselTech
 * @version 1.0.0
 * @since 2024
 */
@Configuration
public class CorsConfig implements WebMvcConfigurer {

    /**
     * Configura o mapeamento CORS para permitir requisições de qualquer origem.
     *
     * @param registry Registro de configurações CORS do Spring
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*");
    }
}
