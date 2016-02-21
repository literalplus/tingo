package li.l1t.tingo.rest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.security.Principal;
import java.util.Collections;
import java.util.Map;

/**
 * REST Controller providing an API for authentication. That API supports logging in, logging out and checking login
 * status.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 2016-02-09
 */
@RestController
public class AuthenticationController {

    @RequestMapping("/auth/status")
    public Principal user(Principal user) { //Spring throws a 401 if not logged in (explicitly only authed users in security config)
        return user;
    }

    @RequestMapping("/auth/token")
    public Map<String, String> token(HttpSession session) {
        return Collections.singletonMap("token", session.getId());
    }
}
