package hr.rba.creditcardservice.api.filter;

import hr.rba.creditcardservice.exception.*;
import hr.rba.creditcardservice.security.*;
import hr.rba.creditcardservice.security.helper.*;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.web.authentication.*;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter{

    private final UserDetailsService userDetailsService;
	private final JwtHelper jwtHelper;

    public JwtAuthenticationFilter(UserDetailsService userDetailsService, JwtHelper jwtHelper) {
        this.userDetailsService = userDetailsService;
        this.jwtHelper = jwtHelper;
    }


    @Override
	protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        final String authHeader = request.getHeader(Constant.AUTHORIZATION);

        try {
            jwtHelper.validateToken(authHeader);
            var jwt = jwtHelper.extractJwtFromAuthHeader(authHeader);
            updateSecurityContext(request, jwt);
            filterChain.doFilter(request, response);
        } catch (TokenValidationException e) {
            response.setContentType("text/plain");
            response.setStatus(e.getStatusCode().value());
            response.getWriter().append(e.getMessage());
        }
	}

    @Override
    protected boolean shouldNotFilter(@NonNull HttpServletRequest request) {

        String path = request.getRequestURI();
        String[] allowedPaths = Constant.PUBLIC_REQUEST_MATCHERS;

        for (var allowedPath : allowedPaths) {
            allowedPath = allowedPath.replace("/**", "");

            if (path.contains(allowedPath)) {
                return true;
            }
        }
        return false;
    }

    private void updateSecurityContext(HttpServletRequest request, String jwt) {
        final String username = jwtHelper.extractUsername(jwt);

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                    userDetails,
                    null,
                    userDetails.getAuthorities()
            );
            authToken.setDetails(
                    new WebAuthenticationDetailsSource().buildDetails(request)
            );
            SecurityContextHolder.getContext().setAuthentication(authToken);
        }
    }
}
