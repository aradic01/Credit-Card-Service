package hr.rba.creditcardservice.helper;

import org.springframework.security.core.authority.*;

public class AuthorityFactory {
    public static SimpleGrantedAuthority getAuthority(String roleName) {
        return new SimpleGrantedAuthority("ROLE_" + roleName);
    }
}
