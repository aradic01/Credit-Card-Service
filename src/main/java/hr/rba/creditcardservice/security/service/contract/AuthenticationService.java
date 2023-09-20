package hr.rba.creditcardservice.security.service.contract;

import hr.rba.creditcardservice.openapi.model.*;

public interface AuthenticationService {
    User register(RegisterRequest registerRequest);
    AuthResponse authenticate(AuthRequest authRequest);
}
