package hr.rba.creditcardservice.service;

import hr.rba.creditcardservice.common.mapper.*;
import hr.rba.creditcardservice.exception.*;
import hr.rba.creditcardservice.jpa.entity.user.*;
import hr.rba.creditcardservice.jpa.repository.*;
import hr.rba.creditcardservice.openapi.model.*;
import hr.rba.creditcardservice.openapi.model.User;
import hr.rba.creditcardservice.service.contract.*;
import lombok.extern.slf4j.*;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.crypto.password.*;
import org.springframework.stereotype.*;

@Service
@Slf4j
public class UserServiceImpl implements UserDetailsService, UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Invalid username!"));
    }

    @Override
    public User registerNewUser(RegisterRequest registerRequest) {
        var user = UserMapper.INSTANCE.mapTo(registerRequest);

        if (userRepository.findByUsernameOrEmail(
                registerRequest.getUsername(),
                registerRequest.getEmail()
        ).isPresent()) {
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
