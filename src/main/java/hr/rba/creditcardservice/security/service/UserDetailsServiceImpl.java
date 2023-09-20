package hr.rba.creditcardservice.security.service;

import hr.rba.creditcardservice.jpa.repository.*;
import lombok.extern.slf4j.*;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.*;

@Service
@Slf4j
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Invalid username!"));
    }
}
