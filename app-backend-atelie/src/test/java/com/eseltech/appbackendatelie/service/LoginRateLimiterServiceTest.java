package com.eseltech.appbackendatelie.service;

import io.github.bucket4j.Bucket;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LoginRateLimiterServiceTest {

    private LoginRateLimiterService rateLimiterService;

    @BeforeEach
    void setUp() {
        rateLimiterService = new LoginRateLimiterService();
    }

    @Test
    void devePermitirCincoTentativasEBloquearASexta() {
        String ipTeste = "192.168.0.1";
        Bucket bucket = rateLimiterService.resolveBucket(ipTeste);

        for (int i = 0; i < 5; i++) {
            assertTrue(bucket.tryConsume(1), "Deveria permitir a tentativa " + (i + 1));
        }

        assertFalse(bucket.tryConsume(1), "Deveria bloquear a 6ª tentativa");
    }

    @Test
    void deveCriarBucketsIndependentesPorIp() {
        Bucket bucketIp1 = rateLimiterService.resolveBucket("10.0.0.1");
        Bucket bucketIp2 = rateLimiterService.resolveBucket("10.0.0.2");

        for (int i = 0; i < 5; i++) {
            bucketIp1.tryConsume(1);
        }
        assertFalse(bucketIp1.tryConsume(1), "IP 1 deve estar bloqueado");

        assertTrue(bucketIp2.tryConsume(1), "O bucket do IP 2 não deve ser afetado pelo IP 1");
    }
}