package hr.rba.creditcardservice.exception;

import org.springframework.http.*;
import org.springframework.web.server.*;

public class TokenValidationException extends ResponseStatusException {
    public TokenValidationException(String message) {
        super(HttpStatus.UNAUTHORIZED, message);
    }
}
