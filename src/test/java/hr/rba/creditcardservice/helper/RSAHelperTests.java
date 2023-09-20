package hr.rba.creditcardservice.helper;

import hr.rba.creditcardservice.security.helper.*;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.test.context.*;

import java.security.interfaces.*;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
public class RSAHelperTests {

    @Autowired
    private RSAHelper rsaHelper;

    @Test
    void keysShouldBeLoaded() {

        var privateKey = rsaHelper.getPrivateKey();
        var publicKey = rsaHelper.getPublicKey();

        assertThat(privateKey).isNotNull();
        assertThat(publicKey).isNotNull();

        assertThat(privateKey).isInstanceOf(RSAPrivateKey.class);
        assertThat(publicKey).isInstanceOf(RSAPublicKey.class);
    }
}
