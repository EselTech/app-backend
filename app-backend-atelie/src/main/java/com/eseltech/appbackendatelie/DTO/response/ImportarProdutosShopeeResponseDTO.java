package com.eseltech.appbackendatelie.DTO.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "DTO de resposta da importação de produtos da Shopee")
public record ImportarProdutosShopeeResponseDTO(
        @Schema(description = "Total de produtos encontrados na Shopee", example = "50")
        int totalEncontrados,

        @Schema(description = "Total de produtos importados com sucesso", example = "45")
        int totalImportados,

        @Schema(description = "Total de produtos que falhou na importação", example = "5")
        int totalFalhas,

        @Schema(description = "Lista de erros ocorridos durante a importação")
        List<ErroImportacao> erros
) {
    @Schema(description = "Detalhes de um erro de importação")
    public record ErroImportacao(
            @Schema(description = "ID do item na Shopee", example = "123456")
            Long itemId,

            @Schema(description = "Nome do produto", example = "Produto Exemplo")
            String nomeProduto,

            @Schema(description = "Mensagem de erro", example = "Campo obrigatório ausente")
            String mensagemErro
    ) {
    }
}

