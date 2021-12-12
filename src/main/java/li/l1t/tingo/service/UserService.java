package li.l1t.tingo.service;

import li.l1t.tingo.config.TingoProperties;
import li.l1t.tingo.exception.JsonPropagatingException;
import li.l1t.tingo.model.GuestUser;
import li.l1t.tingo.model.RegisteredUser;
import li.l1t.tingo.model.User;
import li.l1t.tingo.model.UserAuthority;
import li.l1t.tingo.model.repo.AuthorityRepository;
import li.l1t.tingo.model.repo.UserRepository;
import li.l1t.tingo.security.auth.RegisteredUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.Validate;

import java.security.Principal;

/**
 * Service handling principal registration.
 *
 * @since 2016-02-21
 */
@Service
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final TingoProperties tingoProperties;
    private final AuthorityRepository authorityRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Autowired
    public UserService(
            UserRepository userRepository, TingoProperties tingoProperties,
            AuthorityRepository authorityRepository
    ) {
        this.userRepository = userRepository;
        this.tingoProperties = tingoProperties;
        this.authorityRepository = authorityRepository;
    }

    public User createUser(String username, String password, String registerSecret) {
        if (username.startsWith("*")) { //for guest code
            throw new JsonPropagatingException("Sorry, dein Benutzername darf nicht mit einem Sternchen beginnen.");
        }
        if (!tingoProperties.getRegisterSecret().equalsIgnoreCase(registerSecret)) {
            if (tingoProperties.getGuestCode().equalsIgnoreCase(registerSecret)) {
                throw new JsonPropagatingException("Pssst, falsches Formular! Das ist ein Zugangscode, kein Geheimcode.");
            }
            throw new JsonPropagatingException("Falscher Geheimcode!");
        }

        if (userRepository.findByName(username).isPresent()) {
            throw new JsonPropagatingException("Benutzername schon vergeben!");
        }

        User user = userRepository.save(new RegisteredUser(username, passwordEncoder.encode(password), true));
        authorityRepository.save(new UserAuthority(user.getName(), "default"));
        return user;
    }

    /**
     * Gets a User object from a principal. If the principal is an authenticated guest, a {@link GuestUser} is
     * returned.
     *
     * @param principal the principal to convert
     * @return a user object
     * @throws UsernameNotFoundException if no user with that name exists
     */
    public User from(Principal principal) {
        if (GuestUser.isGuest(principal)) {
            return GuestUser.INSTANCE;
        } else {
            return fromRegistered(principal);
        }
    }

    /**
     * Gets a registered user from a principal. Does not take into account authenticated guests.
     *
     * @param principal the principal to convert
     * @return a user object
     * @throws UsernameNotFoundException if no user with that name exists
     */
    public RegisteredUser fromRegistered(Principal principal) {
        Validate.notNull(principal, "principal");
        return getByName(principal.getName());
    }

    public RegisteredUser getByName(String username) {
        return userRepository.findByName(username)
                .orElseThrow(() -> new UsernameNotFoundException(String.format(
                        "No user found for name %s", username
                )));
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var user = getByName(username);
        var authorities = authorityRepository.getAllByName(username);
        return new RegisteredUserDetails(user, authorities);
    }
}
