package com.atypon.authentication.serucity.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;


@Service
public class JwtService {

    private static final String SECRET = "ed33f0c5b39893b13ff5d4442298c50549c938dea0fc700c2552a78dd4248cbaed900cf03e31b1d561ef6abbf7c8418402c4633342da144191271b2d4fb007812c220f06ea8dda2f5dcf028775a1664c36778e43242a25956462fdf2fc0c760c4fda857c24f1ae0952ba3efc629ecb0414f7a87c3e358f49f91a1cdb0d81ff911c4865c97a7b2981538fae630d071b984ab44ea7fcf86fa4f93ec8cb1be53e963d600d881e0f2e32b361145ac35d81c69945d4baacf1f1260a3fa4c31dc13bd4c99cd96b5fc1f074168899b097e4e9874bfb9d32cd316f76a03d2ad00cfbab6044f9869cd325ae048a97011fecb72465211bb6281929133dd70cf35b608f0f53";

    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        return generateToken(claims, userDetails);
    }

    public String generateToken(
            Map<String, Object> extraClaims,
            UserDetails userDetails
    ) {
        return Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 24))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean isTokenExpired(String token) {
        return extractExpDate(token).before(new Date());
    }
    private Date extractExpDate(String token) {
        return extractClaim(token, Claims::getExpiration);
    }


    public boolean isTokenValid(String token, UserDetails userDetails) {
        String username = extractUsername(token);
        return username.equals(userDetails.getUsername());
    }
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public Long extractIdToken(String token) {
        return extractClaim(token, claims -> claims.get("id", Long.class));
    }


    private Key getSignInKey(){
        byte[] keyBytes = Decoders.BASE64.decode(SECRET);
        return Keys.hmacShaKeyFor(keyBytes);
    }




}
