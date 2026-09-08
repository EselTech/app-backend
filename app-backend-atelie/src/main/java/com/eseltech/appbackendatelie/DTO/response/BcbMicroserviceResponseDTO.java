package com.eseltech.appbackendatelie.DTO.response;

public record BcbMicroserviceResponseDTO (
    String siglaImposto,
    double valorTaxa,
    String dataAtualizacao
){
}
