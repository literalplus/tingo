package li.l1t.tingo.security.auth;

import li.l1t.tingo.model.GuestUser;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

/**
 * User details of a guest user who authenticated without a password.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 2016-05-20
 */
public class GuestUserDetails implements UserDetails {
    public static final GuestUserDetails INSTANCE = new GuestUserDetails();
    private static final Collection<? extends GrantedAuthority> authorities
            = Collections.singleton(new SimpleGrantedAuthority("readonly"));

    private GuestUserDetails() {

    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return "";
    }

    @Override
    public String getUsername() {
        return GuestUser.NAME;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
