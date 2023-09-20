package hr.rba.creditcardservice.security.service;

import hr.rba.creditcardservice.security.service.contract.*;
import org.springframework.security.core.*;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.*;

import java.time.*;
import java.util.stream.*;

@Service
public class TokenServiceImpl implements TokenService {

    private final JwtEncoder jwtEncoder;

    public TokenServiceImpl(JwtEncoder jwtEncoder) {
        this.jwtEncoder = jwtEncoder;
    }

    @Override
    public String generateJwt(Authentication authentication) {
        String scope = authentication.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(" "));

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("aRad")
                .issuedAt(Instant.now())
                .subject(authentication.getName())
                .expiresAt(Instant.now().plusSeconds(1200L))
                .claim("roles", scope)
                .build();

        return jwtEncoder
                .encode(JwtEncoderParameters.from(claims))
                .getTokenValue();
    }
}
