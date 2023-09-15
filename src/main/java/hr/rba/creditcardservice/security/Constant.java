package hr.rba.creditcardservice.security;

public class Constant {
    public static final String AUTHORIZATION = "Authorization";
    public static final String BEARER = "Bearer ";
    public static final String[] PUBLIC_REQUEST_MATCHERS = new String[]{"/auth/**"};
}
