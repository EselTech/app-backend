package com.eseltech.appbackendatelie.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class ShopeeHmacUtil {

    private static final Logger logger = LoggerFactory.getLogger(ShopeeHmacUtil.class);
    private static final String HMAC_SHA256_ALGORITHM = "HmacSHA256";

    public static String generateSignature(String baseString, String partnerKey) {
        try {
            Mac mac = Mac.getInstance(HMAC_SHA256_ALGORITHM);
            SecretKeySpec secretKeySpec = new SecretKeySpec(
                partnerKey.getBytes(StandardCharsets.UTF_8),
                HMAC_SHA256_ALGORITHM
            );
            mac.init(secretKeySpec);

            byte[] hmacBytes = mac.doFinal(baseString.getBytes(StandardCharsets.UTF_8));

            return bytesToHex(hmacBytes);

        } catch (NoSuchAlgorithmException e) {
            logger.error("Algoritmo HMAC-SHA256 não encontrado", e);
            throw new IllegalStateException("Erro ao gerar assinatura: Algoritmo não encontrado", e);
        } catch (InvalidKeyException e) {
            logger.error("Chave inválida para assinatura HMAC", e);
            throw new IllegalStateException("Erro ao gerar assinatura: Chave inválida", e);
        }
    }

    public static String buildBaseString(Long partnerId, String path, Long timestamp) {
        return String.format("%d%s%d", partnerId, path, timestamp);
    }

    public static String buildBaseStringWithToken(Long partnerId, String path, Long timestamp,
                                                   String accessToken, Long shopId) {
        StringBuilder baseString = new StringBuilder();
        baseString.append(partnerId);
        baseString.append(path);
        baseString.append(timestamp);

        if (accessToken != null && !accessToken.isEmpty()) {
            baseString.append(accessToken);
        }

        if (shopId != null) {
            baseString.append(shopId);
        }

        return baseString.toString();
    }

    private static String bytesToHex(byte[] bytes) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : bytes) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }

    public static Long getCurrentTimestamp() {
        return System.currentTimeMillis() / 1000L;
    }
}

