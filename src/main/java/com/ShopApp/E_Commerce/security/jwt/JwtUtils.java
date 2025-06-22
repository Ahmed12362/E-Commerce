package com.ShopApp.E_Commerce.security.jwt;

import com.ShopApp.E_Commerce.security.user.ShopUserDetails;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.List;

@Component
public class JwtUtils {
//    @Value("${auth.token.jwtSecret}")
    private static final String jwtSecret = "uTo9u1mYV2zK3Rj8lWq4fN7bSm9tGx6cPjZrQeThWkEzLyVxBpDnHoAtCvMsUyZq";

    @Value("${auth.token.expirationInMils}")
    private int expirationTime;


    //for debugging
    @PostConstruct
    public void init() {
        System.out.println("JwtUtils initialized. jwtSecret: " + jwtSecret);
        System.out.println("JwtUtils initialized. jwtSecret: " + jwtSecret + ", expirationTime: " + expirationTime);
    }
    //end debugging
    public String generateTokenForUser(Authentication authentication){
        ShopUserDetails userPrinciple =(ShopUserDetails) authentication.getPrincipal();

        List<String> roles = userPrinciple.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority).toList();

        return Jwts.builder()
                .setSubject(userPrinciple.getUsername())
                .claim("id" , userPrinciple.getId())
                .claim("roles" , roles)
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date().getTime() + expirationTime)))
                .signWith(key() , SignatureAlgorithm.HS256)
                .compact();
    }
    private Key key(){
        return Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }
    public String getUserNameFromToken(String token){
       return Jwts.parserBuilder()
                .setSigningKey((key()))
                .build()
                .parseClaimsJws(token)
                .getBody().getSubject();
    }
    public boolean validateToken(String token){
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key())
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException e) {
            throw new RuntimeException(e);
        } catch (UnsupportedJwtException|MalformedJwtException|SignatureException|IllegalArgumentException e) {
            throw new JwtException(e.getMessage());
        }
    }
}
