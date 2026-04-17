package com.notifyguard.notify_service.Notify.jwtsecurity;

import com.notifyguard.notify_service.Notify.entity.Role;
import com.notifyguard.notify_service.Notify.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    public String secret;
    @Value("${jwt.expiration}")
    public Long expiration;
    private Key getSigningKey(){
        return Keys.hmacShaKeyFor(secret.getBytes());
    }

    public String generateToken(User user){
        Map<String,Object> claims=new HashMap<>();
        claims.put("user id",user.getId());
        //claims.put("role",user.getRole().name());
        return Jwts.builder().setClaims(claims).setSubject(user.getEmail())
                .setExpiration(new Date(System.currentTimeMillis()+expiration))
                .setIssuedAt(new Date(System.currentTimeMillis())).
                signWith(getSigningKey(), SignatureAlgorithm.HS256).compact();
    }
    public Claims extractAllClaims(String token){
        return Jwts.parser()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
    public String extractEmail(String token){
        return extractAllClaims(token).getSubject();
    }
//    public String extractRole(String token){
//        return extractAllClaims(token).get("role", String.class);
//    }
    public boolean isTokenExpired(String token){
        return extractAllClaims(token).getExpiration().before(new Date());

    }
    public Boolean validateToken(String token,String email){
        final String extractedEmail=extractEmail(token);
        return (extractedEmail.equals(email)&&!isTokenExpired(token));
    }


}

