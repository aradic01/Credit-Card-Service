package hr.rba.creditcardservice.controller;

import com.fasterxml.jackson.databind.*;
import hr.rba.creditcardservice.exception.*;
import hr.rba.creditcardservice.openapi.model.*;
import hr.rba.creditcardservice.security.service.contract.*;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.test.autoconfigure.web.servlet.*;
import org.springframework.boot.test.context.*;
import org.springframework.boot.test.mock.mockito.*;
import org.springframework.security.authentication.*;
import org.springframework.test.web.servlet.*;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthControllerTests {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private AuthenticationService authenticationService;

    private static final String REGISTER_ENDPOINT = "/auth/register";
    private static final String LOGIN_ENDPOINT = "/auth/login";

    private static User user;

    private static RegisterRequest adminRegisterRequest;
    private static AuthRequest authRequest;
    private static AuthResponse authResponse;

    private static ObjectMapper objectMapper;

    @BeforeAll
    static void init() {

        objectMapper = new ObjectMapper();

        authRequest = new AuthRequest()
                .username("testUser")
                .password("password");

        authResponse = new AuthResponse()
                .accessToken("testToken");

        adminRegisterRequest = new RegisterRequest()
                .email("test@mail.com")
                .firstName("testName")
                .lastName("testLastName")
                .username("testUser")
                .password("password")
                .role(RegisterRequest.RoleEnum.ADMIN);

        user = new User()
                .email("test@mail.com")
                .firstName("testName")
                .lastName("testLastName")
                .username("testUser");

    }


    @Test
    void registerShouldRegisterNewUser() throws Exception {
        when(authenticationService.register(adminRegisterRequest)).thenReturn(user);

        this.mvc.perform(post(REGISTER_ENDPOINT)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(adminRegisterRequest)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(content().string(objectMapper.writeValueAsString(user)));
    }

    @Test
    void registerShouldReturn409WhenUserAlreadyExists() throws Exception {
        when(authenticationService.register(any()))
                .thenThrow(new UserAlreadyExistsException("Already registered!"));

        this.mvc.perform(post(REGISTER_ENDPOINT)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(adminRegisterRequest)))
                .andDo(print())
                .andExpect(status().isConflict());

    }

    @Test
    void loginShouldValidateUserCredentialsAndReturnValidJwt() throws Exception {
        when(authenticationService.authenticate(any())).thenReturn(authResponse);

        this.mvc.perform(post(LOGIN_ENDPOINT)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(authRequest)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(content().string(objectMapper.writeValueAsString(authResponse)));
    }

    @Test
    void loginShouldReturn401UnauthorizedForInvalidCredentials() throws Exception {
        when(authenticationService.authenticate(any()))
                .thenThrow(new BadCredentialsException("Bad credentials"));

        this.mvc.perform(post(LOGIN_ENDPOINT)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(authRequest)))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }
}
