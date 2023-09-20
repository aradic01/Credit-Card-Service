package hr.rba.creditcardservice.security.service.contract;

import org.springframework.security.core.*;

public interface TokenService {
    String generateJwt(Authentication authentication);
}
