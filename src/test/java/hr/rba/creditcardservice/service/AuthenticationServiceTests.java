package hr.rba.creditcardservice.service;

import hr.rba.creditcardservice.exception.*;
import hr.rba.creditcardservice.openapi.model.*;
import hr.rba.creditcardservice.security.service.contract.*;
import hr.rba.creditcardservice.service.contract.*;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.test.context.*;
import org.springframework.boot.test.mock.mockito.*;
import org.springframework.security.authentication.*;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class AuthenticationServiceTests {

    @Autowired
    private AuthenticationService authenticationService;

    @MockBean
    private TokenService tokenService;

    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    private UserService userService;

    private static RegisterRequest registerRequest;
    private static AuthRequest authRequest;
    private static User user;

    @BeforeAll
    static void init() {

        authRequest = new AuthRequest()
                .username("username")
                .password("password");

        registerRequest = new RegisterRequest()
                .username("username")
                .password("password")
                .firstName("firstName")
                .lastName("lastName")
                .email("email@mail.com");

        user = new User()
                .username("username")
                .firstName("firstName")
                .lastName("LastName");
    }

    @Test
    void registerShouldSuccessfullyRegisterNewUser() {
        when(userService.registerNewUser(any())).thenReturn(user);

        var result = authenticationService.register(registerRequest);

        assertThat(result).isInstanceOf(User.class);

        assertThat(result.getFirstName()).isEqualTo(user.getFirstName());
        assertThat(result.getLastName()).isEqualTo(user.getLastName());
        assertThat(result.getEmail()).isEqualTo(user.getEmail());
    }

    @Test
    void registerShouldThrowUserAlreadyExistsException() {
        when(userService.registerNewUser(any())).thenThrow(UserAlreadyExistsException.class);

        assertThrows(UserAlreadyExistsException.class,
                () -> authenticationService.register(registerRequest));
    }

    @Test
    void authenticateShouldSuccessfullyAuthenticateUser() {
        when(authenticationManager.authenticate(any()))
                .thenReturn(
                        new UsernamePasswordAuthenticationToken(
                                user.getUsername(),
                                "password")
                );

        when(tokenService.generateJwt(any())).thenReturn("token");

        var responseExpected = new AuthResponse()
                .accessToken("token");

        assertThat(authenticationService.authenticate(authRequest)).isEqualTo(responseExpected);
    }

    @Test
    void authenticateShouldThrowAuthenticationException() {
        when(authenticationManager.authenticate(any())).thenThrow(BadCredentialsException.class);

        assertThrows(BadCredentialsException.class,
                () -> authenticationService.authenticate(authRequest));
    }
}
