package hr.rba.creditcardservice.jpa.entity.user;

import lombok.*;
import org.springframework.security.core.authority.*;

import java.util.*;
import java.util.stream.*;

@Getter
@RequiredArgsConstructor
public enum Role {

    USER, ADMIN, MANAGER;

    public List<SimpleGrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(this.name()));
    }
}
