package hr.rba.creditcardservice.security.helper;

import hr.rba.creditcardservice.exception.*;
import jakarta.annotation.*;
import lombok.*;
import lombok.extern.slf4j.*;
import org.springframework.stereotype.*;

import java.security.*;
import java.security.interfaces.*;

@Component
@Getter
@Slf4j
public class RSAHelper {

    private RSAPublicKey publicKey;
    private RSAPrivateKey privateKey;

    @PostConstruct
    private void loadKeys() throws InternalErrorException {

        log.info("Inside loadKeys..");

        KeyPair keypair;
        try {
            keypair = generateRsaKeypair();
        } catch (NoSuchAlgorithmException e) {
            log.error("Invalid algorithm exception: {}", e.getMessage());
            throw new InternalErrorException(e.getMessage(), e.getCause());
        }
        this.publicKey = (RSAPublicKey) keypair.getPublic();
        this.privateKey = (RSAPrivateKey) keypair.getPrivate();
    }

    public KeyPair generateRsaKeypair() throws NoSuchAlgorithmException {

        log.info("Generating RSA keypair..");

        KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
        generator.initialize(2048);
        return generator.generateKeyPair();
    }
}
