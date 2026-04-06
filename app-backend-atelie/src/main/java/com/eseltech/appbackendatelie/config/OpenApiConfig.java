package com.eseltech.appbackendatelie.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
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
        final String securitySchemeName = "bearerAuth";

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
                                
                                ## Autenticação JWT
                                A API utiliza autenticação baseada em JWT (JSON Web Token).
                                
                                ### Como autenticar:
                                1. Faça login no endpoint `/auth/login` com suas credenciais
                                2. Copie o token retornado na resposta
                                3. Clique no botão "Authorize" (🔒) no topo da página
                                4. Digite: `Bearer <seu_token>` e confirme
                                5. Agora você pode acessar os endpoints protegidos
                                
                                ### Perfis de Acesso:
                                - **ADMIN**: Acesso total, incluindo registro de novos usuários
                                - **USER**: Acesso aos endpoints de consulta e operações básicas
                                
                                ## Integração BCB
                                A API integra-se com o Sistema Gerenciador de Séries Temporais (SGS) do Banco Central
                                para obter valores atualizados de indicadores econômicos e tributários.
                                """))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:8080")
                                .description("Servidor de Desenvolvimento")
                ))
                .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
                .components(new Components()
                        .addSecuritySchemes(securitySchemeName,
                                new SecurityScheme()
                                        .name(securitySchemeName)
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                                        .description("Insira o token JWT no formato: Bearer {token}")
                        )
                );
    }
}

