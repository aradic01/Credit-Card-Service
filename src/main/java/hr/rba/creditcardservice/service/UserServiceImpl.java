package hr.rba.creditcardservice.service;

import hr.rba.creditcardservice.common.mapper.*;
import hr.rba.creditcardservice.exception.*;
import hr.rba.creditcardservice.jpa.entity.user.*;
import hr.rba.creditcardservice.jpa.repository.*;
import hr.rba.creditcardservice.openapi.model.*;
import hr.rba.creditcardservice.service.contract.*;
import lombok.extern.slf4j.*;
import org.springframework.security.crypto.password.*;
import org.springframework.stereotype.*;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User registerNewUser(RegisterRequest registerRequest) {

        log.info("*** Registering new user ***");

        var user = UserMapper.INSTANCE.mapTo(registerRequest);

        if (userRepository.existsByUsernameOrEmail(
                registerRequest.getUsername(),
                registerRequest.getEmail()
        )) {
            throw new UserAlreadyExistsException("User already registered!");
        }

        var passwordEncoded = passwordEncoder.encode(registerRequest.getPassword());
        user.setPassword(passwordEncoded);

        setDefaultUserRoleIfNotProvided(user);

        return UserMapper.INSTANCE.mapTo(userRepository.save(user));
    }

    private void setDefaultUserRoleIfNotProvided(UserEntity user) {
        if (user.getRole() == null || user.getRole().name().isEmpty()) {
            user.setRole(Role.USER);
        }
    }
}
