package hr.rba.creditcardservice.service;

import hr.rba.creditcardservice.common.*;
import hr.rba.creditcardservice.jpa.entity.user.*;
import hr.rba.creditcardservice.jpa.repository.*;
import hr.rba.creditcardservice.openapi.model.*;
import hr.rba.creditcardservice.service.contract.*;
import hr.rba.creditcardservice.service.contract.TokenService;
import org.springframework.security.authentication.*;
import org.springframework.security.core.*;
import org.springframework.security.core.token.*;
import org.springframework.security.crypto.password.*;
import org.springframework.stereotype.*;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private  final TokenService tokenService;
    private final PasswordEncoder passwordEncoder;

    public AuthenticationServiceImpl(UserRepository userRepository, AuthenticationManager authenticationManager, TokenService tokenService, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User register(RegisterRequest registerRequest) {
        var user = UserMapper.INSTANCE.mapTo(registerRequest);

        var passwordEncoded = passwordEncoder.encode(registerRequest.getPassword());
        user.setPassword(passwordEncoded);

        return UserMapper.INSTANCE.mapTo(userRepository.save(user));
    }

    @Override
    public AuthResponse authenticate(AuthRequest registerRequest) {
        Authentication authentication = authenticationManager
                .authenticate(
                        new UsernamePasswordAuthenticationToken(
                                registerRequest.getUsername(),
                                registerRequest.getPassword())
                );

        String jwt = tokenService.generateJwt(authentication);

        return new AuthResponse().accessToken(jwt);
    }
}
