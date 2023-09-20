package hr.rba.creditcardservice.service.contract;

import hr.rba.creditcardservice.openapi.model.*;

public interface UserService {
    User registerNewUser(RegisterRequest registerRequest);
}
