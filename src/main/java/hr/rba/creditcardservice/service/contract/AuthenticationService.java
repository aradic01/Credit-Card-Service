package hr.rba.creditcardservice.service.contract;

import hr.rba.creditcardservice.openapi.model.AuthResponse;
import hr.rba.creditcardservice.openapi.model.AuthRequest;
import hr.rba.creditcardservice.openapi.model.RegisterRequest;

public interface AuthenticationService {
    AuthResponse register(RegisterRequest registerRequest);
    AuthResponse login(AuthRequest registerRequest);
}
