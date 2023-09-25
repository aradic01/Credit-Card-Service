package hr.rba.creditcardservice.api.filter;

import hr.rba.creditcardservice.security.properties.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import org.slf4j.*;
import org.springframework.lang.*;
import org.springframework.stereotype.*;
import org.springframework.web.filter.*;

import java.io.*;

@Component
public class ApiLoggingFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(
           @NonNull HttpServletRequest request,
           @NonNull HttpServletResponse response,
           @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        var userId = request
                .getUserPrincipal()
                .getName();
        try {
            MDC.put("userId", userId);
            filterChain.doFilter(request, response);
        } finally {
            MDC.clear();
        }
    }

    @Override
    protected boolean shouldNotFilter(@NonNull HttpServletRequest request) {
        String path = request.getRequestURI();
        String[] allowedPaths = SecurityProperties.PUBLIC_URL_MATCHERS;

        for (var allowedPath : allowedPaths) {
            allowedPath = allowedPath.replace("/**", "");

            if (path.contains(allowedPath)) {
                return true;
            }
        }
        return false;
    }
}
