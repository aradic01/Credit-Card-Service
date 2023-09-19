package hr.rba.creditcardservice.service.contract;

import org.springframework.security.core.*;

public interface TokenService {
    String generateJwt(Authentication authentication);
}
