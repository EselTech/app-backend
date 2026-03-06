package com.eseltech.appbackendatelie.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Configuração do OpenAPI/Swagger para documentação da API.
 * Esta classe configura as informações gerais da documentação da API REST.
 */
@Configuration
public class OpenApiConfig {

    /**
     * Configura o objeto OpenAPI com informações detalhadas sobre a API.
     *
     * @return Configuração personalizada do OpenAPI
     */
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API Ateliê - Sistema de Gestão de Impostos")
                        .version("1.0.0")
                        .description("""
                                API REST para gerenciamento de impostos e integração com o Banco Central do Brasil (BCB).
                                
                                ## Funcionalidades principais:
                                - Consulta de impostos cadastrados
                                - Atualização automática de valores via API do BCB
                                - Gerenciamento de histórico de impostos
                                - Inserção e exclusão de impostos
                                
                                ## Integração BCB
                                A API integra-se com o Sistema Gerenciador de Séries Temporais (SGS) do Banco Central
                                para obter valores atualizados de indicadores econômicos e tributários.
                                """))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:8080")
                                .description("Servidor de Desenvolvimento")
                ));
    }
}

