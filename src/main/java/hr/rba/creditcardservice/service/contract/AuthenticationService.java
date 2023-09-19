package hr.rba.creditcardservice.service.contract;

import hr.rba.creditcardservice.openapi.model.*;

public interface AuthenticationService {
    User register(RegisterRequest registerRequest);
    AuthResponse authenticate(AuthRequest registerRequest);
}
