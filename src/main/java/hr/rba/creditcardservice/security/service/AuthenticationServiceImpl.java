package hr.rba.creditcardservice.security.service;

import hr.rba.creditcardservice.openapi.model.*;
import hr.rba.creditcardservice.security.service.contract.*;
import hr.rba.creditcardservice.service.contract.*;
import lombok.extern.slf4j.*;
import org.springframework.security.authentication.*;
import org.springframework.security.core.*;
import org.springframework.stereotype.*;

@Service
@Slf4j
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;

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

        log.info("Authenticating user with username {}", authRequest.getUsername());

        Authentication authentication = authenticationManager
                .authenticate(
                        new UsernamePasswordAuthenticationToken(
                                authRequest.getUsername(),
                                authRequest.getPassword()
                        )
                );

        log.info("User authenticated!");

        return new AuthResponse()
                .accessToken(tokenService.generateJwt(authentication));
    }
}
