package hr.rba.creditcardservice.service;

import hr.rba.creditcardservice.jpa.entity.user.*;
import hr.rba.creditcardservice.security.service.contract.*;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.test.context.*;
import org.springframework.security.authentication.*;
import org.springframework.security.oauth2.jwt.*;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
public class TokenServiceTests {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private JwtDecoder jwtDecoder;

    private static UserEntity user;

    @BeforeAll
    static void init() {
        user = UserEntity.builder()
                .email("test@mail.com")
                .firstName("testName")
                .lastName("testLastName")
                .username("testUser")
                .password("password")
                .role(Role.ADMIN)
                .build();
    }

    @Test
    void generateJwtShouldReturnAValidToken() {
        var authToken = new UsernamePasswordAuthenticationToken(
                user,
                null,
                user.getAuthorities()
        );

        var jwt = tokenService.generateJwt(authToken);
        var jwtDecoded = jwtDecoder.decode(jwt);

        var usernameExtractedFromJwt = jwtDecoded.getClaim("sub").toString();
        var rolesExtractedFromJwt = jwtDecoded.getClaim("roles").toString();

        assertThat(usernameExtractedFromJwt).isEqualTo(user.getUsername());
        assertThat(rolesExtractedFromJwt).isEqualTo(user.getRole().name());
    }
}
