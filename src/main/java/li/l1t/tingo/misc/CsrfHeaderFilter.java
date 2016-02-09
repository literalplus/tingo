package li.l1t.tingo.misc;

import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.WebUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Filters requests, adding the CSRF token as a cookie, if not present already. AngularJS does the rest.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 2016-02-10
 */
public class CsrfHeaderFilter extends OncePerRequestFilter {
    public static final String COOKIE_NAME = "XSRF-TOKEN";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        CsrfToken csrf = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
        if(csrf != null) {
            Cookie cookie = WebUtils.getCookie(request, COOKIE_NAME);
            String token = csrf.getToken();
            if(cookie == null || ((token != null) && !token.equals(cookie.getValue()))) {
                cookie = new Cookie("XSRF-TOKEN", token);
                cookie.setPath("/");
                response.addCookie(cookie);
            }
        }

        filterChain.doFilter(request, response);
    }
}
