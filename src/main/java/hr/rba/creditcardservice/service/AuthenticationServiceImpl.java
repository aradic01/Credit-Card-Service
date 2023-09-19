package hr.rba.creditcardservice.service;

import hr.rba.creditcardservice.openapi.model.*;
import hr.rba.creditcardservice.service.contract.*;
import hr.rba.creditcardservice.service.contract.TokenService;
import org.springframework.security.authentication.*;
import org.springframework.security.core.*;
import org.springframework.stereotype.*;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private  final TokenService tokenService;

    public AuthenticationServiceImpl(UserService userService, AuthenticationManager authenticationManager, TokenService tokenService) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
    }

    @Override
    public User register(RegisterRequest registerRequest) {
        return userService.registerNewUser(registerRequest);
    }

    @Override
    public AuthResponse authenticate(AuthRequest authRequest) {
        Authentication authentication = authenticationManager
                .authenticate(
                        new UsernamePasswordAuthenticationToken(
                                authRequest.getUsername(),
                                authRequest.getPassword()
                        )
                );

        return new AuthResponse()
                .accessToken(tokenService.generateJwt(authentication));
    }
}
