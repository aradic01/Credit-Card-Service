package hr.rba.creditcardservice.exception;

import java.security.*;

public class InternalErrorException extends Throwable {
    public InternalErrorException(String message, Throwable e) {
        super(message, e);
    }
}
