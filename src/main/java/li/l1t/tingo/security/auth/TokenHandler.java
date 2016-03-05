package li.l1t.tingo.security.auth;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import li.l1t.tingo.misc.PersistentTokenGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.sql.Date;
import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * Handles generation and validation of JWT tokens using the jjwt library.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 2016-03-05
 */
@Service
public class TokenHandler {
    @Autowired
    private PersistentTokenGenerator tokenGenerator;

    @Autowired
    private UserDetailsService userDetailsService;

    public UserDetails parseUserFromToken(String token) {
        String username = Jwts.parser()
                .setSigningKey(tokenGenerator.getToken())
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
        return userDetailsService.loadUserByUsername(username);
    }

    public String createTokenForUser(Principal user) {
        return Jwts.builder()
                .setSubject(user.getName())
                .signWith(SignatureAlgorithm.HS256, tokenGenerator.getToken())
                .setExpiration(Date.from(LocalDateTime.now().plusDays(7).atZone(ZoneId.systemDefault()).toInstant()))
                .compact();
    }
}
