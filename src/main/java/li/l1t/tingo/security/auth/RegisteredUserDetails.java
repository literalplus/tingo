package li.l1t.tingo.security.auth;

import li.l1t.tingo.model.RegisteredUser;
import li.l1t.tingo.model.UserAuthority;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

/**
 * User details for a registered user.
 *
 * @since 2021-12-12
 */
public class RegisteredUserDetails implements UserDetails {
    private final transient RegisteredUser delegate;
    private final List<SimpleGrantedAuthority> authorities;

    public RegisteredUserDetails(RegisteredUser delegate, List<UserAuthority> authorities) {
        this.delegate = delegate;
        this.authorities = authorities.stream()
                .map(it -> new SimpleGrantedAuthority(it.getAuthority()))
                .toList();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return delegate.getPassword();
    }

    @Override
    public String getUsername() {
        return delegate.getName();
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
        return delegate.isEnabled();
    }
}
