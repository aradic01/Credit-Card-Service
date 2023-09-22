package hr.rba.creditcardservice.security.properties;

public class SecurityProperties {
    public static final String[] PUBLIC_URL_MATCHERS = new String[] {"/auth/**"};
    public static final String[] PROTECTED_URL_MATCHERS = new String[] {"/person/**", "/file/**"};
}
