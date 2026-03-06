package com.eseltech.appbackendatelie;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Classe principal da aplicação Spring Boot.
 * Responsável por inicializar o contexto da aplicação e configurar os beans necessários.
 *
 * <p>Esta aplicação fornece uma API REST para gerenciamento de impostos com integração
 * ao Banco Central do Brasil (BCB) para consulta de valores atualizados.</p>
 *
 * <p>Documentação da API disponível em: <code>/swagger-ui.html</code></p>
 *
 * @author EselTech
 * @version 1.0.0
 * @since 2024
 */
@SpringBootApplication
@OpenAPIDefinition(
        info = @Info(
                title = "API Ateliê - Sistema de Gestão de Impostos",
                version = "1.0.0",
                description = "API REST para gerenciamento de impostos e integração com o Banco Central do Brasil"
        )
)
public class AppBackendAtelieApplication {

    /**
     * Método principal que inicia a aplicação Spring Boot.
     *
     * @param args Argumentos de linha de comando
     */
    public static void main(String[] args) {
        SpringApplication.run(AppBackendAtelieApplication.class, args);
    }

}
