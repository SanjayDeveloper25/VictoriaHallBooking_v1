package com.gcc.communityHall.component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expirationMs}")
    private int jwtExpirationMs;

    @PostConstruct
    public void init() {
       // System.out.println("jwtSecret: " + jwtSecret);
       // System.out.println("jwtExpirationMs: " + jwtExpirationMs);
    }

    public String generateToken(String userType, int hallId, long mobileNumber, String employeeId, String zone) {
        try {
            //System.out.println("generate token here");
            String token= Jwts.builder()
                    .claim("userType", userType)
                    .claim("hallId", hallId)
                    .claim("mobileNumber", mobileNumber)
                    .claim("employeeId", employeeId)
                    .claim("zone", zone)
                    .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
                    .signWith(SignatureAlgorithm.HS512, jwtSecret)
                    .compact();
            //System.out.println("Generated JWT Token: " + token);
            return token;
        } catch (Exception e) {
            System.err.println("Failed to generate JWT token: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public boolean validateToken(String token) {
    	//System.out.println("validation starts");
        try {
        	//System.out.println("inside validation try");
            Claims claims = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody();
            return !isTokenExpired(claims);
        } catch (ExpiredJwtException e) {
            System.err.println("JWT token has expired: " + e.getMessage());
            return false;
        } catch (JwtException | IllegalArgumentException e) {
            System.err.println("JWT token validation failed: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    private boolean isTokenExpired(Claims claims) {
        return claims.getExpiration().before(new Date());
    }
    
}
