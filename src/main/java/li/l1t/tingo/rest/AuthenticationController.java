package li.l1t.tingo.rest;

import li.l1t.tingo.config.TingoConfiguration;
import li.l1t.tingo.exception.AuthException;
import li.l1t.tingo.model.dto.AuthenticationDto;
import li.l1t.tingo.security.auth.TokenHandler;
import li.l1t.tingo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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
    @Autowired
    private UserService userService;
    @Autowired
    private TokenHandler tokenHandler;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private TingoConfiguration configuration;

    @RequestMapping("/auth/status")
    public Principal user(Principal user) { //Spring throws a 401 if not logged in (explicitly only authed users in security config)
        return user;
    }

    @RequestMapping("/auth/token")
    public Map<String, String> token(HttpSession session) {
        return Collections.singletonMap("token", session.getId());
    }

    @RequestMapping(value = "/auth/register", method = RequestMethod.POST)
    public Map<String, Boolean> register(@RequestBody AuthenticationDto request) {
        userService.createUser(request.getUsername(), request.getPassword(), request.getRegisterToken());
        return Collections.singletonMap("success", true);
    }

    @RequestMapping(value = "/auth/login", method = RequestMethod.POST)
    public Map<String, String> login(@RequestBody AuthenticationDto request) {
        Authentication authentication =
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword());
        Principal user;
        try {
            user = authenticationManager.authenticate(authentication);
        } catch (BadCredentialsException e) {
            throw new AuthException("Benutzername und/oder Passwort falsch!");
        } catch (AuthenticationException e) {
            throw new AuthException("Fehler beim Login: " + e.getClass().getSimpleName());
        }
        String token = tokenHandler.createTokenForUser(user);
        return Collections.singletonMap("token", token);
    }

    @RequestMapping(value = "/auth/guest", method = RequestMethod.POST)
    public Map<String, String> guestAuth(@RequestBody AuthenticationDto request) {
        if (configuration.getGuestCode().equalsIgnoreCase(request.getPassword())) {
            return Collections.singletonMap("token", tokenHandler.createGuestToken());
        } else if(configuration.getRegisterSecret().equalsIgnoreCase(request.getPassword())) {
            throw new AuthException("Du bist zu Größerem bestimmt! Das ist ein VIP-Code, " +
                    "du kannst dir damit rechts einen Account anlegen.");
        } else {
            throw new AuthException("Invalider Zugangscode!");
        }
    }
}
