package hr.rba.creditcardservice.service;

import hr.rba.creditcardservice.jpa.repository.*;
import hr.rba.creditcardservice.security.*;
import hr.rba.creditcardservice.security.helper.*;
import jakarta.servlet.http.*;
import lombok.extern.slf4j.*;
import org.springframework.security.core.*;
import org.springframework.security.web.authentication.logout.*;
import org.springframework.stereotype.*;

@Service
@Slf4j
public class LogoutService implements LogoutHandler {

    private final TokenRepository tokenRepository;
    private final JwtHelper jwtHelper;

    public LogoutService(TokenRepository tokenRepository, JwtHelper jwtHelper) {
        this.tokenRepository = tokenRepository;
        this.jwtHelper = jwtHelper;
    }

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {

        log.info("Logging out..");

        final String authHeader = request.getHeader(Constant.AUTHORIZATION);

        jwtHelper.validateToken(authHeader);

        final String jwt = jwtHelper.extractJwtFromAuthHeader(authHeader);

        var storedToken = tokenRepository.findByToken(jwt)
                .orElse(null);

        if (storedToken != null) {
            storedToken.setExpired(true);
            storedToken.setRevoked(true);

            tokenRepository.save(storedToken);
        }
    }
}
