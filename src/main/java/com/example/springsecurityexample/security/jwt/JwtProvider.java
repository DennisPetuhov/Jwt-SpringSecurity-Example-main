package com.example.springsecurityexample.security.jwt;

import com.example.springsecurityexample.security.services.UserPrinciple;

import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.security.SignatureException;
import java.util.Date;

@Component
public class JwtProvider {

    private static final Logger logger = LoggerFactory.getLogger(JwtProvider.class); //todo

    @Value("${grokonez.app.jwtSecret}") // анотация читаем по ключу(AplicationProperty) записываем знаечение
    private String jwtSecret;

    @Value("${grokonez.app.jwtExpiration}") // анотация читаем по ключу(AplicationProperty) записываем знаечение
    private int jwtExpiration;

    public String generateJwtToken(Authentication authentication) { // получаем обьект аутентификации

        UserPrinciple userPrincipal = (UserPrinciple) authentication.getPrincipal(); // из аутенфификации получаем пользователя

        return Jwts.builder() // библиотека JWTS создаем токкен
		                .setSubject((userPrincipal.getUsername()))
		                .setIssuedAt(new Date()) // создание токена
		                .setExpiration(new Date((new Date()).getTime() + jwtExpiration*1000)) // срок хранения токена
		                .signWith(SignatureAlgorithm.HS512, jwtSecret)// алгоритм шифрования/ секретное слово
		                .compact(); // удалить пробелы, создать компактную запись
    }
    
    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);//разбираем на части/добавляемтся секретное слово/проверяется на правильность и в случае успеха приходит true
            return true;
        } catch (MalformedJwtException e) {
            logger.error("Invalid JWT token -> Message: {}", e);
        } catch (ExpiredJwtException e) {
            logger.error("Expired JWT token -> Message: {}", e);
        } catch (UnsupportedJwtException e) {
            logger.error("Unsupported JWT token -> Message: {}", e);
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims string is empty -> Message: {}", e);
        }
        
        return false;
    }
    
    public String getUserNameFromJwtToken(String token) {
        return Jwts.parser()
			                .setSigningKey(jwtSecret) // подмешиваем секретный ключ
			                .parseClaimsJws(token) // парсим(извлекаем данные), дешифруем токен
			                .getBody().getSubject(); // достаем  поле Payload, затем достаем поле Sub(поле токена)
    }
}