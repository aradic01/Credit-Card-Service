package hr.rba.creditcardservice.security.helper;

import hr.rba.creditcardservice.exception.*;
import jakarta.annotation.*;
import lombok.*;
import org.springframework.stereotype.*;

import java.security.*;
import java.security.interfaces.*;

@Component
@Getter
public class RSAHelper {

    private RSAPublicKey publicKey;
    private RSAPrivateKey privateKey;

    @PostConstruct
    private void loadKeys() throws InternalErrorException {
        KeyPair keypair;
        try {
            keypair = generateRsaKeypair();
        } catch (NoSuchAlgorithmException e) {
            throw new InternalErrorException(e.getMessage(), e.getCause());
        }
        this.publicKey = (RSAPublicKey) keypair.getPublic();
        this.privateKey = (RSAPrivateKey) keypair.getPrivate();
    }

    public KeyPair generateRsaKeypair() throws NoSuchAlgorithmException {
        KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
        generator.initialize(2048);
        return generator.generateKeyPair();
    }
}
