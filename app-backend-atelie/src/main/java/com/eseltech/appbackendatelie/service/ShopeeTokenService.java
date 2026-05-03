package com.eseltech.appbackendatelie.service;

import com.eseltech.appbackendatelie.DTO.request.ShopeeRefreshTokenRequestDTO;
import com.eseltech.appbackendatelie.DTO.response.ShopeeRefreshTokenResponseDTO;
import com.eseltech.appbackendatelie.config.ShopeeConfig;
import com.eseltech.appbackendatelie.entity.ShopeeTokenInfo;
import com.eseltech.appbackendatelie.exceptions.ShopeeTokenNotFoundException;
import com.eseltech.appbackendatelie.exceptions.ShopeeTokenRefreshException;
import com.eseltech.appbackendatelie.repository.ShopeeTokenInfoRepository;
import com.eseltech.appbackendatelie.util.ShopeeHmacUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDateTime;
import java.time.Duration;

@Service
public class ShopeeTokenService {

    private static final Logger logger = LoggerFactory.getLogger(ShopeeTokenService.class);

    private final ShopeeTokenInfoRepository tokenRepository;
    private final ShopeeConfig shopeeConfig;
    private final WebClient webClient;

    @Autowired
    public ShopeeTokenService(ShopeeTokenInfoRepository tokenRepository,
                             ShopeeConfig shopeeConfig,
                             WebClient.Builder webClientBuilder) {
        this.tokenRepository = tokenRepository;
        this.shopeeConfig = shopeeConfig;
        this.webClient = webClientBuilder.build();
    }

    @Transactional
    public String getValidAccessToken(Long shopId) {
        logger.info("Buscando access token válido para shopId: {}", shopId);

        // Busca a credencial no banco
        ShopeeTokenInfo tokenInfo = tokenRepository.findByShopId(shopId)
                .orElseThrow(() -> {
                    logger.error("Token não encontrado para shopId: {}", shopId);
                    return new ShopeeTokenNotFoundException(
                        String.format("Credenciais não encontradas para a loja com ID: %d", shopId)
                    );
                });

        // Verifica se o token está expirando nos próximos 15 minutos
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expirationThreshold = now.plusMinutes(shopeeConfig.getRefreshMarginMinutes());

        if (tokenInfo.getExpiresAt().isBefore(expirationThreshold)) {
            logger.info("Token expirando em breve para shopId: {}. Iniciando renovação...", shopId);
            return refreshShopeeToken(tokenInfo);
        }

        logger.info("Token válido encontrado para shopId: {}", shopId);
        return tokenInfo.getAccessToken();
    }

    private String refreshShopeeToken(ShopeeTokenInfo tokenInfo) {
        logger.info("Iniciando renovação de token para shopId: {}", tokenInfo.getShopId());

        try {
            Long timestamp = ShopeeHmacUtil.getCurrentTimestamp();

            String baseString = ShopeeHmacUtil.buildBaseString(
                shopeeConfig.getPartnerId(),
                shopeeConfig.getRefreshTokenPath(),
                timestamp
            );

            String signature = ShopeeHmacUtil.generateSignature(baseString, shopeeConfig.getPartnerKey());

            ShopeeRefreshTokenRequestDTO requestBody = new ShopeeRefreshTokenRequestDTO(
                tokenInfo.getShopId(),
                tokenInfo.getRefreshToken(),
                shopeeConfig.getPartnerId()
            );

            String url = String.format("%s?partner_id=%d&timestamp=%d&sign=%s",
                shopeeConfig.getRefreshTokenUrl(),
                shopeeConfig.getPartnerId(),
                timestamp,
                signature
            );

            logger.debug("Fazendo requisição para renovar token. URL: {}", url);

            ShopeeRefreshTokenResponseDTO response = webClient.post()
                    .uri(url)
                    .bodyValue(requestBody)
                    .retrieve()
                    .bodyToMono(ShopeeRefreshTokenResponseDTO.class)
                    .timeout(Duration.ofSeconds(30))
                    .block();

            if (response == null) {
                logger.error("Resposta nula recebida da API Shopee para shopId: {}", tokenInfo.getShopId());
                throw new ShopeeTokenRefreshException("Resposta nula recebida da API Shopee");
            }

            if (response.hasError()) {
                logger.error("Erro ao renovar token da Shopee. Código: {}, Mensagem: {}",
                    response.getError(), response.getMessage());
                throw new ShopeeTokenRefreshException(
                    String.format("Erro da API Shopee: %s - %s", response.getError(), response.getMessage())
                );
            }

            tokenInfo.setAccessToken(response.getAccessToken());
            tokenInfo.setRefreshToken(response.getRefreshToken());

            LocalDateTime newExpiresAt = LocalDateTime.now().plusHours(4);
            tokenInfo.setExpiresAt(newExpiresAt);

            tokenRepository.save(tokenInfo);

            logger.info("Token renovado com sucesso para shopId: {}. Nova expiração: {}",
                tokenInfo.getShopId(), newExpiresAt);

            return tokenInfo.getAccessToken();

        } catch (ShopeeTokenRefreshException e) {
            throw e;
        } catch (Exception e) {
            logger.error("Erro inesperado ao renovar token da Shopee para shopId: {}",
                tokenInfo.getShopId(), e);
            throw new ShopeeTokenRefreshException(
                "Erro ao renovar token da Shopee: " + e.getMessage(), e
            );
        }
    }

    @Transactional
    public ShopeeTokenInfo saveTokenInfo(ShopeeTokenInfo tokenInfo) {
        logger.info("Salvando credenciais para shopId: {}", tokenInfo.getShopId());
        tokenInfo.setExpiresAt(LocalDateTime.now().plusHours(4));
        return tokenRepository.save(tokenInfo);
    }

    public ShopeeTokenInfo getTokenInfo(Long shopId) {
        return tokenRepository.findByShopId(shopId)
                .orElseThrow(() -> new ShopeeTokenNotFoundException(
                    String.format("Credenciais não encontradas para a loja com ID: %d", shopId)
                ));
    }

    public boolean hasTokenInfo(Long shopId) {
        return tokenRepository.existsByShopId(shopId);
    }

    @Transactional
    public void deleteTokenInfo(Long shopId) {
        logger.info("Removendo credenciais para shopId: {}", shopId);
        ShopeeTokenInfo tokenInfo = getTokenInfo(shopId);
        tokenRepository.delete(tokenInfo);
        logger.info("Credenciais removidas com sucesso para shopId: {}", shopId);
    }
}


