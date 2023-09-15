package hr.rba.creditcardservice.security.helper;

import hr.rba.creditcardservice.exception.*;
import hr.rba.creditcardservice.jpa.entity.token.*;
import hr.rba.creditcardservice.jpa.repository.*;
import hr.rba.creditcardservice.security.*;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.*;
import io.jsonwebtoken.security.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.*;

import java.security.*;
import java.util.*;
import java.util.function.*;

@Component
public class JwtHelper {

    @Value("${application.security.jwt.secret-key}")
    private String secretKey;
    @Value("${application.security.jwt.expiration}")
    private long jwtExpirationInMillis;
    @Value("${application.security.jwt.refresh-token.expiration}")
    private long refreshExpirationInMillis;

    private final TokenRepository tokenRepository;

    public JwtHelper(TokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }


    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }

    private String generateToken(
            HashMap<String, Object> extraClaims,
            UserDetails userDetails
    ) {
        return buildToken(extraClaims, userDetails, jwtExpirationInMillis);
    }

    public String generateRefreshToken(UserDetails userDetails) {
        return buildToken(new HashMap<>(), userDetails, refreshExpirationInMillis);
    }

    private String buildToken(
            HashMap<String, Object> extraClaims,
            UserDetails userDetails,
            long jwtExpirationInMillis
    ) {
        return Jwts
                .builder()
                .setHeaderParam("typ", "JWT")
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationInMillis))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public void validateToken(String authHeader) {

        var token = extractJwtFromAuthHeader(authHeader);

        if (authHeader.isBlank() || !authHeader.startsWith(Constant.BEARER) || token.isBlank()) {
            throw new TokenValidationException("Token invalid!");
        }

        var isTokenRevoked = tokenRepository.findByToken(token)
                .map(Token::isRevoked)
                .orElse(false);

        if (isTokenExpired(token) || isTokenRevoked) {
            throw new TokenValidationException("Token invalid!");
        }
    }

    public String extractJwtFromAuthHeader(String authHeader) {
        return authHeader.substring(7);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
