package hr.rba.creditcardservice.service;

import hr.rba.creditcardservice.common.mapper.*;
import hr.rba.creditcardservice.jpa.entity.token.*;
import hr.rba.creditcardservice.jpa.entity.user.*;
import hr.rba.creditcardservice.jpa.repository.*;
import hr.rba.creditcardservice.openapi.model.AuthResponse;
import hr.rba.creditcardservice.openapi.model.RegisterRequest;
import hr.rba.creditcardservice.openapi.model.AuthRequest;
import hr.rba.creditcardservice.security.helper.*;
import hr.rba.creditcardservice.service.contract.*;
import org.springframework.security.authentication.*;
import org.springframework.security.crypto.password.*;
import org.springframework.stereotype.*;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final JwtHelper jwtHelper;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;

    public AuthenticationServiceImpl(UserRepository userRepository, TokenRepository tokenRepository, JwtHelper jwtHelper, AuthenticationManager authenticationManager, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.tokenRepository = tokenRepository;
        this.jwtHelper = jwtHelper;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public AuthResponse register(RegisterRequest registerRequest) {
        var user = UserMapper.INSTANCE.mapTo(registerRequest);

        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));

        var savedUser = userRepository.save(user);
        var jwtToken = jwtHelper.generateToken(user);
        var refreshToken = jwtHelper.generateRefreshToken(user);

        saveUserToken(savedUser, jwtToken);

        return new AuthResponse()
                .accessToken(jwtToken)
                .refreshToken(refreshToken);
    }

    @Override
    public AuthResponse login(AuthRequest registerRequest) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        registerRequest.getUsername(),
                        registerRequest.getPassword()
                )
        );

        var user = userRepository.findByUsername(registerRequest.getUsername())
                .orElseThrow();
        var jwtToken = jwtHelper.generateToken(user);
        var refreshToken = jwtHelper.generateRefreshToken(user);

        revokeAllUserTokens(user);
        saveUserToken(user, jwtToken);

        return new AuthResponse()
                .accessToken(jwtToken)
                .refreshToken(refreshToken);
    }

    private void saveUserToken(UserEntity user, String jwtToken) {
        var token = Token.builder()
                .user(user)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .revoked(false)
                .build();
        tokenRepository.save(token);
    }

    private void revokeAllUserTokens(UserEntity user) {
        var validUserTokens = tokenRepository.findAllValidTokensByUserId(user.getUserId());
        if (validUserTokens.isEmpty())
            return;
        validUserTokens.forEach(token -> {
            token.setRevoked(true);
        });
        tokenRepository.saveAll(validUserTokens);
    }

//    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
//        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
//        final String refreshToken;
//        final String username;
//        if (authHeader == null ||!authHeader.startsWith("Bearer ")) {
//            return;
//        }
//        refreshToken = authHeader.substring(7);
//        username = jwtHelper.extractUsername(refreshToken);
//        if (username != null) {
//            var user = this.userRepository.findByUsername(username)
//                    .orElseThrow();
//            if (jwtHelper.validateToken(refreshToken)) {
//                var accessToken = jwtHelper.generateToken(user);
//                revokeAllUserTokens(user);
//                saveUserToken(user, accessToken);
//                var authResponse = new AuthResponse()
//                        .accessToken(accessToken)
//                        .refreshToken(refreshToken);
//
//                new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
//            }
//        }
//    }
}
