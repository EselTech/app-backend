package com.eseltech.appbackendatelie.service;

import com.eseltech.appbackendatelie.config.ShopeeConfig;
import com.eseltech.appbackendatelie.util.ShopeeHmacUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@Service
public class ShopeeApiService {

    private static final Logger logger = LoggerFactory.getLogger(ShopeeApiService.class);

    private final ShopeeTokenService shopeeTokenService;
    private final ShopeeConfig shopeeConfig;
    private final WebClient webClient;

    @Autowired
    public ShopeeApiService(ShopeeTokenService shopeeTokenService,
                            ShopeeConfig shopeeConfig,
                            WebClient.Builder webClientBuilder) {
        this.shopeeTokenService = shopeeTokenService;
        this.shopeeConfig = shopeeConfig;
        this.webClient = webClientBuilder.build();
    }

    public Map<String, Object> buscarListaProdutos(Long shopId) {
        logger.info("Buscando lista de produtos para shopId: {}", shopId);

        try {
            String accessToken = shopeeTokenService.getValidAccessToken(shopId);
            logger.debug("Access token obtido com sucesso");

            String path = "/api/v2/product/get_item_list";
            Long timestamp = ShopeeHmacUtil.getCurrentTimestamp();
            Long partnerId = shopeeConfig.getPartnerId();

            String baseString = ShopeeHmacUtil.buildBaseStringWithToken(
                partnerId,
                path,
                timestamp,
                accessToken,
                shopId
            );

            String signature = ShopeeHmacUtil.generateSignature(baseString, shopeeConfig.getPartnerKey());
            logger.debug("Assinatura gerada: {}", signature);

            String url = String.format(
                "%s%s?partner_id=%d&timestamp=%d&access_token=%s&shop_id=%d&sign=%s&offset=0&page_size=10",
                shopeeConfig.getBaseUrl(),
                path,
                partnerId,
                timestamp,
                accessToken,
                shopId,
                signature
            );

            logger.info("Fazendo requisição para: {}", path);
            Map<String, Object> response = webClient.get()
                    .uri(url)
                    .retrieve()
                    .bodyToMono(Map.class)
                    .timeout(Duration.ofSeconds(30))
                    .block();

            logger.info("Lista de produtos obtida com sucesso para shopId: {}", shopId);
            return response;

        } catch (Exception e) {
            logger.error("Erro ao buscar lista de produtos para shopId: {}", shopId, e);
            throw new RuntimeException("Erro ao buscar produtos na Shopee: " + e.getMessage(), e);
        }
    }

    public Map<String, Object> obterDetalhesProduto(Long shopId, Long itemId) {
        logger.info("Buscando detalhes do produto {} para shopId: {}", itemId, shopId);

        try {
            String accessToken = shopeeTokenService.getValidAccessToken(shopId);

            String path = "/api/v2/product/get_item_base_info";
            Long timestamp = ShopeeHmacUtil.getCurrentTimestamp();
            Long partnerId = shopeeConfig.getPartnerId();

            String baseString = ShopeeHmacUtil.buildBaseStringWithToken(
                partnerId,
                path,
                timestamp,
                accessToken,
                shopId
            );
            String signature = ShopeeHmacUtil.generateSignature(baseString, shopeeConfig.getPartnerKey());

            String url = String.format(
                "%s%s?partner_id=%d&timestamp=%d&access_token=%s&shop_id=%d&sign=%s&item_id_list=%d",
                shopeeConfig.getBaseUrl(),
                path,
                partnerId,
                timestamp,
                accessToken,
                shopId,
                signature,
                itemId
            );

            Map<String, Object> response = webClient.get()
                    .uri(url)
                    .retrieve()
                    .bodyToMono(Map.class)
                    .timeout(Duration.ofSeconds(30))
                    .block();

            logger.info("Detalhes do produto {} obtidos com sucesso", itemId);
            return response;

        } catch (Exception e) {
            logger.error("Erro ao obter detalhes do produto {} para shopId: {}", itemId, shopId, e);
            throw new RuntimeException("Erro ao obter detalhes do produto: " + e.getMessage(), e);
        }
    }

    public Map<String, Object> atualizarEstoque(Long shopId, Long itemId, Integer stock) {
        logger.info("Atualizando estoque do produto {} para {} unidades", itemId, stock);

        try {
            String accessToken = shopeeTokenService.getValidAccessToken(shopId);

            String path = "/api/v2/product/update_stock";
            Long timestamp = ShopeeHmacUtil.getCurrentTimestamp();
            Long partnerId = shopeeConfig.getPartnerId();

            String baseString = ShopeeHmacUtil.buildBaseStringWithToken(
                partnerId,
                path,
                timestamp,
                accessToken,
                shopId
            );
            String signature = ShopeeHmacUtil.generateSignature(baseString, shopeeConfig.getPartnerKey());

            String url = String.format(
                "%s%s?partner_id=%d&timestamp=%d&access_token=%s&shop_id=%d&sign=%s",
                shopeeConfig.getBaseUrl(),
                path,
                partnerId,
                timestamp,
                accessToken,
                shopId,
                signature
            );

            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("item_id", itemId);
            requestBody.put("stock_list", new Object[]{
                Map.of("stock", stock)
            });

            Map<String, Object> response = webClient.post()
                    .uri(url)
                    .bodyValue(requestBody)
                    .retrieve()
                    .bodyToMono(Map.class)
                    .timeout(Duration.ofSeconds(30))
                    .block();

            logger.info("Estoque atualizado com sucesso para o produto {}", itemId);
            return response;

        } catch (Exception e) {
            logger.error("Erro ao atualizar estoque do produto {} para shopId: {}", itemId, shopId, e);
            throw new RuntimeException("Erro ao atualizar estoque: " + e.getMessage(), e);
        }
    }

    public Map<String, Object> fazerRequisicaoGet(Long shopId, String path, Map<String, String> additionalParams) {
        logger.info("Fazendo requisição GET para: {}", path);

        try {
            String accessToken = shopeeTokenService.getValidAccessToken(shopId);

            Long timestamp = ShopeeHmacUtil.getCurrentTimestamp();
            Long partnerId = shopeeConfig.getPartnerId();

            String baseString = ShopeeHmacUtil.buildBaseStringWithToken(
                partnerId, path, timestamp, accessToken, shopId
            );
            String signature = ShopeeHmacUtil.generateSignature(baseString, shopeeConfig.getPartnerKey());

            StringBuilder urlBuilder = new StringBuilder();
            urlBuilder.append(shopeeConfig.getBaseUrl())
                    .append(path)
                    .append("?partner_id=").append(partnerId)
                    .append("&timestamp=").append(timestamp)
                    .append("&access_token=").append(accessToken)
                    .append("&shop_id=").append(shopId)
                    .append("&sign=").append(signature);

            if (additionalParams != null) {
                additionalParams.forEach((key, value) ->
                    urlBuilder.append("&").append(key).append("=").append(value)
                );
            }

            // Fazer requisição
            return webClient.get()
                    .uri(urlBuilder.toString())
                    .retrieve()
                    .bodyToMono(Map.class)
                    .timeout(Duration.ofSeconds(30))
                    .block();

        } catch (Exception e) {
            logger.error("Erro ao fazer requisição GET para {}", path, e);
            throw new RuntimeException("Erro na requisição à API Shopee: " + e.getMessage(), e);
        }
    }

    public Map<String, Object> fazerRequisicaoPost(Long shopId, String path, Map<String, Object> requestBody) {
        logger.info("Fazendo requisição POST para: {}", path);

        try {
            String accessToken = shopeeTokenService.getValidAccessToken(shopId);

            Long timestamp = ShopeeHmacUtil.getCurrentTimestamp();
            Long partnerId = shopeeConfig.getPartnerId();

            String baseString = ShopeeHmacUtil.buildBaseStringWithToken(
                partnerId, path, timestamp, accessToken, shopId
            );
            String signature = ShopeeHmacUtil.generateSignature(baseString, shopeeConfig.getPartnerKey());

            String url = String.format(
                "%s%s?partner_id=%d&timestamp=%d&access_token=%s&shop_id=%d&sign=%s",
                shopeeConfig.getBaseUrl(),
                path,
                partnerId,
                timestamp,
                accessToken,
                shopId,
                signature
            );

            return webClient.post()
                    .uri(url)
                    .bodyValue(requestBody)
                    .retrieve()
                    .bodyToMono(Map.class)
                    .timeout(Duration.ofSeconds(30))
                    .block();

        } catch (Exception e) {
            logger.error("Erro ao fazer requisição POST para {}", path, e);
            throw new RuntimeException("Erro na requisição à API Shopee: " + e.getMessage(), e);
        }
    }
}

