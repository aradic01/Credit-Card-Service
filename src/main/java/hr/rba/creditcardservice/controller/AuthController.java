package hr.rba.creditcardservice.controller;

import hr.rba.creditcardservice.openapi.api.*;
import hr.rba.creditcardservice.openapi.model.*;
import hr.rba.creditcardservice.service.contract.*;
import org.springframework.http.*;
import org.springframework.stereotype.*;
import org.springframework.validation.annotation.*;

@Controller
@Validated
public class AuthController implements AuthApi {

    private final AuthenticationService authenticationService;

    public AuthController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @Override
    public ResponseEntity<AuthResponse> login(AuthRequest authRequest) {
        return ResponseEntity.ok().body(authenticationService.authenticate(authRequest));
    }

    @Override
    public ResponseEntity<User> register(RegisterRequest registerRequest) {
        return ResponseEntity.ok().body(authenticationService.register(registerRequest));
    }
}
