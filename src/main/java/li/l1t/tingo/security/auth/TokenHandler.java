package li.l1t.tingo.security.auth;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import li.l1t.tingo.misc.PersistentTokenGenerator;
import li.l1t.tingo.model.GuestUser;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.Date;
import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * Handles generation and validation of JWT tokens using the jjwt library.
 *
 * @since 2016-03-05
 */
@Service
public class TokenHandler {
    private final PersistentTokenGenerator tokenGenerator;
    private final UserDetailsService userDetailsService;

    public TokenHandler(PersistentTokenGenerator tokenGenerator, UserDetailsService userDetailsService) {
        this.tokenGenerator = tokenGenerator;
        this.userDetailsService = userDetailsService;
    }

    public UserDetails parseUserFromToken(String token) {
        String username = Jwts.parser()
                .setSigningKey(tokenGenerator.getToken())
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
        if(GuestUser.NAME.equals(username)) {
            return GuestUserDetails.INSTANCE;
        }
        return userDetailsService.loadUserByUsername(username);
    }

    public String createTokenForUser(Principal user) {
        return Jwts.builder()
                .setSubject(user.getName())
                .signWith(SignatureAlgorithm.HS256, tokenGenerator.getToken())
                .setExpiration(Date.from(LocalDateTime.now().plusDays(7).atZone(ZoneId.systemDefault()).toInstant()))
                .compact();
    }

    public String createGuestToken() {
        return Jwts.builder()
                .setSubject(GuestUser.NAME)
                .signWith(SignatureAlgorithm.HS256, tokenGenerator.getToken())
                .setExpiration(Date.from(LocalDateTime.now().plusDays(2).atZone(ZoneId.systemDefault()).toInstant()))
                .compact();
    }
}
