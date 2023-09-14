package hr.rba.creditcardservice.api.filter;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class JwtAuthenticationFilter {
//
//	public static final String AUTHORIZATION = "Authorization";
//	public static final String BEARER = "Bearer ";
//
//	@Autowired
//	private JwtService jwtTokenService;
//
//
//	@Override
//	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
//
//		final Optional<String> jwt = getJwtFromRequest(request);
//		jwt.ifPresent(token -> {
//			try {
//				if (jwtTokenService.isTokenValid(token)) {
//					setSecurityContext(new WebAuthenticationDetailsSource().buildDetails(request), token);
//				}
//			} catch (IllegalArgumentException | MalformedJwtException | ExpiredJwtException e) {
//				logger.error("Unable to get JWT Token or JWT Token has expired");
//				//UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken("anonymous", "anonymous", null);
//				//SecurityContextHolder.getContext().setAuthentication(authentication);
//			}
//		});
//
//		filterChain.doFilter(request, response);
//	}
//
//	private void setSecurityContext(WebAuthenticationDetails authDetails, String token) {
//
//		final String username = jwtTokenService.extractUsername(token);
//		final List<String> roles = jwtTokenService.extractRoles(token);
//
//		final UserDetails userDetails = new User
//				(
//						username,
//				"",
//						roles
//								.stream()
//								.map(SimpleGrantedAuthority::new)
//								.collect(Collectors.toList())
//				);
//
//		final UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null,
//				userDetails.getAuthorities());
//		authentication.setDetails(authDetails);
//		// After setting the Authentication in the context, we specify
//		// that the current user is authenticated. So it passes the
//		// Spring Security Configurations successfully.
//		SecurityContextHolder.getContext().setAuthentication(authentication);
//	}
//
//	private static Optional<String> getJwtFromRequest(HttpServletRequest request) {
//		String bearerToken = request.getHeader(AUTHORIZATION);
//		if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER)) {
//			return Optional.of(bearerToken.substring(7));
//		}
//		return Optional.empty();
//	}
//
//	@Override
//	protected boolean shouldNotFilter(@NonNull HttpServletRequest request) {
//
//		String path = request.getRequestURI();
//		String[] allowedPaths = SecurityConfig.PUBLIC_REQUEST_MATCHERS;
//
//		for (var allowedPath : allowedPaths) {
//			allowedPath = allowedPath.replace("/*", "");
//			allowedPath = allowedPath.replace("/**", "");
//
//			if (path.contains(allowedPath)) {
//				return true;
//			}
//		}
//		return false;
//	}

}
