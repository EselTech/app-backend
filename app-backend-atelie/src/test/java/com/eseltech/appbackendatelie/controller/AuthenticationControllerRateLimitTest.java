package com.eseltech.appbackendatelie.controller;

import com.eseltech.appbackendatelie.DTO.AuthenticationDTO;
import com.eseltech.appbackendatelie.service.LoginRateLimiterService;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthenticationControllerRateLimitTest {

    @Mock
    private LoginRateLimiterService rateLimiterService;

    // TODO: Adicione @Mock para TokenService, AuthenticationManager e outros serviços injetados no controller

    @InjectMocks
    private AuthenticationController authenticationController;

    private MockHttpServletRequest request;

    @BeforeEach
    void setUp() {
        request = new MockHttpServletRequest();
        request.setRemoteAddr("127.0.0.1");
    }

    @Test
    void deveRetornarTooManyRequestsQuandoExcederLimite() {
        Bucket bucketMock = Bucket.builder()
                .addLimit(Bandwidth.classic(1, Refill.greedy(1, Duration.ofMinutes(1))))
                .build();

        when(rateLimiterService.resolveBucket(anyString())).thenReturn(bucketMock);

        AuthenticationDTO dto = new AuthenticationDTO("usuario_teste", "senha_teste");

        try {
            authenticationController.logar(dto, request);
        } catch (Exception e) {
        }

        ResponseEntity<?> response = authenticationController.logar(dto, request);

        assertEquals(HttpStatus.TOO_MANY_REQUESTS, response.getStatusCode());
        assertEquals("Muitas tentativas de login falhas. Por favor, aguarde 1 minuto e tente novamente.", response.getBody());
    }
}