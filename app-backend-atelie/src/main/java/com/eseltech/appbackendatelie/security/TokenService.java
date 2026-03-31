package com.eseltech.appbackendatelie.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.eseltech.appbackendatelie.modal.Usuario;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * Serviço responsável pela geração e validação de tokens JWT.
 * Implementa o padrão Access Token (curta duração) e Refresh Token (longa duração).
 */
@Service
public class TokenService {

    private static final String ISSUER = "app-backend-atelie";
    private static final String TOKEN_TYPE_CLAIM = "tipo";
    private static final String ACCESS_TOKEN_TYPE = "access";
    private static final String REFRESH_TOKEN_TYPE = "refresh";

    @Value("${api.security.token.secret}")
    public String secret;

    /**
     * Gera um Access Token com expiração de 15 minutos.
     *
     * @param usuario o usuário para o qual o token será gerado
     * @return o Access Token JWT
     */
    public String gerarAccessToken(Usuario usuario) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.create()
                    .withIssuer(ISSUER)
                    .withSubject(usuario.getUsername())
                    .withClaim(TOKEN_TYPE_CLAIM, ACCESS_TOKEN_TYPE)
                    .withExpiresAt(gerarDataExpiracaoAccessToken())
                    .sign(algorithm);
        } catch (JWTCreationException exception) {
            throw new RuntimeException("Erro ao gerar Access Token: " + exception);
        }
    }

    /**
     * Gera um Refresh Token com expiração de 7 dias.
     *
     * @param usuario o usuário para o qual o token será gerado
     * @return o Refresh Token JWT
     */
    public String gerarRefreshToken(Usuario usuario) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.create()
                    .withIssuer(ISSUER)
                    .withSubject(usuario.getUsername())
                    .withClaim(TOKEN_TYPE_CLAIM, REFRESH_TOKEN_TYPE)
                    .withExpiresAt(gerarDataExpiracaoRefreshToken())
                    .sign(algorithm);
        } catch (JWTCreationException exception) {
            throw new RuntimeException("Erro ao gerar Refresh Token: " + exception);
        }
    }

    /**
     * Valida um Access Token e retorna o subject (username).
     *
     * @param token o token JWT a ser validado
     * @return o subject do token (username) ou string vazia se inválido
     */
    public String validarAccessToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            DecodedJWT jwt = JWT.require(algorithm)
                    .withIssuer(ISSUER)
                    .withClaim(TOKEN_TYPE_CLAIM, ACCESS_TOKEN_TYPE)
                    .build()
                    .verify(token);
            return jwt.getSubject();
        } catch (JWTVerificationException exception) {
            return "";
        }
    }

    /**
     * Valida um Refresh Token e retorna o subject (username).
     *
     * @param token o token JWT a ser validado
     * @return o subject do token (username) ou string vazia se inválido
     */
    public String validarRefreshToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            DecodedJWT jwt = JWT.require(algorithm)
                    .withIssuer(ISSUER)
                    .withClaim(TOKEN_TYPE_CLAIM, REFRESH_TOKEN_TYPE)
                    .build()
                    .verify(token);
            return jwt.getSubject();
        } catch (JWTVerificationException exception) {
            return "";
        }
    }

    /**
     * Gera a data de expiração para Access Tokens (15 minutos).
     */
    private Instant gerarDataExpiracaoAccessToken() {
        return LocalDateTime.now().plusMinutes(15).atZone(ZoneId.systemDefault()).toInstant();
    }

    /**
     * Gera a data de expiração para Refresh Tokens (7 dias).
     */
    private Instant gerarDataExpiracaoRefreshToken() {
        return LocalDateTime.now().plusDays(7).atZone(ZoneId.systemDefault()).toInstant();
    }
}
