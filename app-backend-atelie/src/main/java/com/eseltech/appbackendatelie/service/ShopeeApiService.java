package com.eseltech.appbackendatelie.service;

import com.eseltech.appbackendatelie.DTO.response.ImportarProdutosShopeeResponseDTO;
import com.eseltech.appbackendatelie.config.ShopeeConfig;
import com.eseltech.appbackendatelie.util.ShopeeHmacUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ShopeeApiService {

    private static final Logger logger = LoggerFactory.getLogger(ShopeeApiService.class);

    private final ShopeeTokenService shopeeTokenService;
    private final ShopeeConfig shopeeConfig;
    private final WebClient webClient;
    private final ProdutoService produtoService;

    @Autowired
    public ShopeeApiService(ShopeeTokenService shopeeTokenService,
                            ShopeeConfig shopeeConfig,
                            WebClient.Builder webClientBuilder,
                            ProdutoService produtoService) {
        this.shopeeTokenService = shopeeTokenService;
        this.shopeeConfig = shopeeConfig;
        this.webClient = webClientBuilder.build();
        this.produtoService = produtoService;
    }

    public Map<String, Object> buscarListaProdutos(Long shopId, Integer offset, Integer pageSize) {
        logger.info("Buscando lista de produtos para shopId: {} (offset: {}, pageSize: {})", shopId, offset, pageSize);

        int offsetValue = (offset != null && offset >= 0) ? offset : 0;
        int pageSizeValue = (pageSize != null && pageSize > 0) ? pageSize : 10;

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
                "%s%s?partner_id=%d&timestamp=%d&access_token=%s&shop_id=%d&sign=%s&offset=%d&page_size=%d&item_status=NORMAL",
                shopeeConfig.getBaseUrl(),
                path,
                partnerId,
                timestamp,
                accessToken,
                shopId,
                signature,
                offsetValue,
                pageSizeValue
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

    public ImportarProdutosShopeeResponseDTO importarProdutosParaBanco(Long shopId, Long empresaId, Integer pageSize) {
        logger.info("Iniciando importação de produtos da Shopee para shopId: {} e empresaId: {}", shopId, empresaId);

        int pageSizeValue = (pageSize != null && pageSize > 0) ? pageSize : 100;
        List<ImportarProdutosShopeeResponseDTO.ErroImportacao> erros = new ArrayList<>();
        int totalEncontrados = 0;
        int totalImportados = 0;
        int offset = 0;
        boolean hasMore = true;

        try {
            while (hasMore) {
                Map<String, Object> listaResponse = buscarListaProdutos(shopId, offset, pageSizeValue);

                @SuppressWarnings("unchecked")
                Map<String, Object> responseData = (Map<String, Object>) listaResponse.get("response");

                if (responseData == null) {
                    logger.error("Resposta da API Shopee não contém dados válidos");
                    break;
                }

                @SuppressWarnings("unchecked")
                List<Map<String, Object>> items = (List<Map<String, Object>>) responseData.get("item");

                if (items == null || items.isEmpty()) {
                    logger.info("Nenhum produto encontrado no offset {}", offset);
                    hasMore = false;
                    break;
                }

                totalEncontrados += items.size();
                logger.info("Processando {} produtos (offset: {})", items.size(), offset);

                for (Map<String, Object> item : items) {
                    Long itemId = null;
                    String nomeProduto = "Desconhecido";

                    try {
                        itemId = getLongFromMap(item, "item_id");
                        if (itemId == null) {
                            logger.warn("Item sem ID encontrado, pulando...");
                            continue;
                        }

                        Map<String, Object> detalhesResponse;
                        try {
                            detalhesResponse = obterDetalhesProduto(shopId, itemId);
                        } catch (Exception e) {
                            // Se falhar ao obter detalhes, registra e continua com o próximo
                            String errorMsg = extrairMensagemErro(e);
                            logger.warn("Erro ao buscar detalhes do produto {}: {}", itemId, errorMsg);
                            erros.add(new ImportarProdutosShopeeResponseDTO.ErroImportacao(
                                itemId, nomeProduto, "Erro ao buscar detalhes: " + errorMsg
                            ));
                            continue;
                        }

                        @SuppressWarnings("unchecked")
                        Map<String, Object> detalhesData = (Map<String, Object>) detalhesResponse.get("response");

                        if (detalhesData == null) {
                            erros.add(new ImportarProdutosShopeeResponseDTO.ErroImportacao(
                                itemId, nomeProduto, "Resposta da API não contém dados válidos"
                            ));
                            continue;
                        }

                        String error = (String) detalhesData.get("error");
                        if (error != null && !error.isEmpty()) {
                            String message = (String) detalhesData.get("message");
                            erros.add(new ImportarProdutosShopeeResponseDTO.ErroImportacao(
                                itemId, nomeProduto, "Erro da API: " + (message != null ? message : error)
                            ));
                            continue;
                        }

                        @SuppressWarnings("unchecked")
                        List<Map<String, Object>> itemList = (List<Map<String, Object>>) detalhesData.get("item_list");

                        if (itemList == null || itemList.isEmpty()) {
                            erros.add(new ImportarProdutosShopeeResponseDTO.ErroImportacao(
                                itemId, nomeProduto, "Lista de detalhes vazia"
                            ));
                            continue;
                        }

                        Map<String, Object> detalhes = itemList.get(0);

                        String nome = (String) detalhes.get("item_name");
                        String descricao = (String) detalhes.get("description");
                        nomeProduto = nome != null ? nome : nomeProduto;

                        BigDecimal preco = BigDecimal.ZERO;
                        Object priceInfoObj = detalhes.get("price_info");
                        
                        logger.debug("Extraindo preço do produto {} - price_info type: {}", 
                                    itemId, priceInfoObj != null ? priceInfoObj.getClass().getSimpleName() : "null");

                        if (priceInfoObj instanceof List) {
                            @SuppressWarnings("unchecked")
                            List<Map<String, Object>> priceInfoList = (List<Map<String, Object>>) priceInfoObj;
                            logger.debug("price_info é List com {} elementos", priceInfoList.size());
                            if (!priceInfoList.isEmpty()) {
                                Map<String, Object> priceInfo = priceInfoList.get(0);
                                Object currentPrice = priceInfo.get("current_price");
                                logger.debug("current_price extraído: {} (type: {})", 
                                           currentPrice, currentPrice != null ? currentPrice.getClass().getSimpleName() : "null");
                                if (currentPrice != null) {
                                    preco = new BigDecimal(currentPrice.toString())
                                            .setScale(2, RoundingMode.HALF_UP);
                                    logger.info("Preço obtido para produto {}: {} (valor original: {})",
                                               itemId, preco, currentPrice);
                                }
                            }
                        } else if (priceInfoObj instanceof Map) {
                            @SuppressWarnings("unchecked")
                            Map<String, Object> priceInfo = (Map<String, Object>) priceInfoObj;
                            Object currentPrice = priceInfo.get("current_price");
                            logger.debug("current_price extraído (Map): {} (type: {})", 
                                       currentPrice, currentPrice != null ? currentPrice.getClass().getSimpleName() : "null");
                            if (currentPrice != null) {
                                preco = new BigDecimal(currentPrice.toString())
                                        .setScale(2, RoundingMode.HALF_UP);
                                logger.info("Preço obtido para produto {}: {} (valor original: {})",
                                           itemId, preco, currentPrice);
                            }
                        } else {
                            logger.warn("price_info não é nem List nem Map para produto {}", itemId);
                        }

                        if (preco.compareTo(BigDecimal.ZERO) == 0) {
                            logger.warn("ATENÇÃO: Preço zerado para produto {} - {}", itemId, nome);
                        }

                        if (nome == null || nome.trim().isEmpty()) {
                            erros.add(new ImportarProdutosShopeeResponseDTO.ErroImportacao(
                                itemId, nomeProduto, "Nome do produto não encontrado"
                            ));
                            continue;
                        }

                        if (descricao == null || descricao.trim().isEmpty()) {
                            descricao = nome;
                        }

                        nome = truncateString(nome, 100);
                        descricao = truncateString(descricao, 100);

                        logger.debug("Produto da Shopee - ID: {}, Nome: {}, Preço: {}", itemId, nome, preco);

                        produtoService.salvarProdutoShopee(empresaId, nome, descricao, preco);
                        totalImportados++;

                        logger.debug("Produto importado com sucesso: {} (ID Shopee: {})", nome, itemId);

                    } catch (Exception e) {
                        String errorMsg = extrairMensagemErro(e);
                        erros.add(new ImportarProdutosShopeeResponseDTO.ErroImportacao(
                            itemId, nomeProduto, errorMsg
                        ));
                        logger.error("Erro ao importar produto {} ({}): {}", itemId, nomeProduto, errorMsg);
                    }
                }

                Boolean more = (Boolean) responseData.get("has_next_page");
                hasMore = Boolean.TRUE.equals(more);

                if (hasMore) {
                    Object nextOffsetObj = responseData.get("next_offset");
                    offset = nextOffsetObj != null ? Integer.parseInt(nextOffsetObj.toString()) : offset + pageSizeValue;
                }
            }

            int totalFalhas = totalEncontrados - totalImportados;
            logger.info("Importação concluída. Total: {}, Importados: {}, Falhas: {}",
                       totalEncontrados, totalImportados, totalFalhas);

            return new ImportarProdutosShopeeResponseDTO(
                totalEncontrados,
                totalImportados,
                totalFalhas,
                erros
            );

        } catch (Exception e) {
            logger.error("Erro crítico durante importação de produtos", e);
            throw new RuntimeException("Erro ao importar produtos da Shopee: " + e.getMessage(), e);
        }
    }

    private Long getLongFromMap(Map<String, Object> map, String key) {
        Object value = map.get(key);
        if (value == null) {
            return null;
        }
        if (value instanceof Long) {
            return (Long) value;
        }
        if (value instanceof Integer) {
            return ((Integer) value).longValue();
        }
        if (value instanceof String) {
            try {
                return Long.parseLong((String) value);
            } catch (NumberFormatException e) {
                return null;
            }
        }
        return null;
    }

    private String truncateString(String str, int maxLength) {
        if (str == null) {
            return null;
        }
        return str.length() > maxLength ? str.substring(0, maxLength) : str;
    }

    private String extrairMensagemErro(Exception e) {
        String message = e.getMessage();
        if (message == null) {
            return e.getClass().getSimpleName();
        }

        if (message.contains("error_busi") || message.contains("error")) {
            return message.length() > 200 ? message.substring(0, 200) + "..." : message;
        }

        return message.length() > 300 ? message.substring(0, 300) + "..." : message;
    }
}

