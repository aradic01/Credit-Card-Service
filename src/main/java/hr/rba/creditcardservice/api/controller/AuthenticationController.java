package hr.rba.creditcardservice.api.controller;

import hr.rba.creditcardservice.openapi.api.AuthApi;
import hr.rba.creditcardservice.openapi.model.AuthRequest;
import hr.rba.creditcardservice.openapi.model.AuthResponse;
import hr.rba.creditcardservice.openapi.model.RegisterRequest;
import hr.rba.creditcardservice.service.contract.*;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;

@Controller
@Validated
public class AuthenticationController implements AuthApi {

    private final AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @Override
    public ResponseEntity<AuthResponse> login(AuthRequest authRequest) {
        return ResponseEntity.ok()
                .body(authenticationService.login(authRequest));
    }

    @Override
    public ResponseEntity<AuthResponse> register(RegisterRequest registerRequest) {
        return ResponseEntity.ok()
                .body(authenticationService.register(registerRequest));
    }
}
