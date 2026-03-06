package com.eseltech.appbackendatelie.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

import org.springframework.http.HttpHeaders;

/**
 * Configuração do WebClient para integração com a API do Banco Central do Brasil (BCB).
 * Define o cliente HTTP reativo para realizar chamadas à API pública do BCB.
 *
 * <p>Esta configuração estabelece:</p>
 * <ul>
 *   <li>URL base da API do BCB</li>
 *   <li>Headers padrão para as requisições</li>
 *   <li>Configurações de serialização JSON</li>
 * </ul>
 *
 * @author EselTech
 * @version 1.0.0
 * @since 2024
 * @see <a href="https://api.bcb.gov.br">API do Banco Central do Brasil</a>
 */
@Configuration
public class BcbConfig {

    /**
     * Cria um builder de WebClient reutilizável.
     *
     * @return Builder do WebClient
     */
    @Bean
    public WebClient.Builder webClientBuilder() {
        return WebClient.builder();
    }

    /**
     * Cria e configura o WebClient para comunicação com a API do BCB.
     *
     * @param builder Builder do WebClient injetado pelo Spring
     * @return WebClient configurado para a API do BCB
     */
    @Bean
    public WebClient bcbWebClient(WebClient.Builder builder) {
        return builder
                .baseUrl("https://api.bcb.gov.br")
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }
}
