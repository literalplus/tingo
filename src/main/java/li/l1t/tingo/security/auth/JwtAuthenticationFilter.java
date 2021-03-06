package li.l1t.tingo.security.auth;

import io.jsonwebtoken.SignatureException;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;

/**
 * Request filter that checks JWT tokens on requests to secured pages.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 2016-03-05
 */
public class JwtAuthenticationFilter extends AbstractAuthenticationProcessingFilter {
    private static final String BEARER_AUTHENTICATION = "Bearer ";
    private final TokenHandler tokenHandler;

    public JwtAuthenticationFilter(TokenHandler tokenHandler) {
        super(request -> request.getRequestURI().startsWith("/api") ||
                request.getRequestURI().startsWith("/secure"));
        this.tokenHandler = tokenHandler;

        //Do nothing on authentication since we authenticate every request
        setAuthenticationSuccessHandler((request, response, authentication) -> {
        });

        //Return 401 Unauthorized if the user is not authenticated
        setAuthenticationFailureHandler((request, response, exception) ->
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized"));
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response)
            throws AuthenticationException, IOException, ServletException {
        String jwtToken = null;
        String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (header != null && header.startsWith(BEARER_AUTHENTICATION)) {
            jwtToken = header.substring(BEARER_AUTHENTICATION.length());
        }
        if((jwtToken == null || jwtToken.isEmpty()) && request.getCookies() != null) {
            Cookie cookie = Arrays.stream(request.getCookies())
                    .filter(c -> c.getName().equals("tingo-token-cookie"))
                    .findFirst().orElse(null);
            if(cookie != null) {
                jwtToken = cookie.getValue();
            }
        }
        if(jwtToken == null || jwtToken.isEmpty()) {
            throw new InsufficientAuthenticationException("Missing JWT token");
        }

        UserDetails userDetails;
        try {
            userDetails = tokenHandler.parseUserFromToken(jwtToken);
        } catch (SignatureException e) { //Generated before server restart - Signature doesn't match
            throw new InsufficientAuthenticationException("Invalid JWT token");
        }

        UserAuthentication authentication = new UserAuthentication(userDetails);
        authentication.setAuthenticated(true);
        return authentication;
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain, Authentication authResult)
            throws IOException, ServletException {
        super.successfulAuthentication(request, response, chain, authResult);

        //Continue processing the request normally - Spring would love to redirect to a login page or something,
        //but we don't need that stuff since we're ~~stateless~~ auth
        chain.doFilter(request, response);
    }
}
