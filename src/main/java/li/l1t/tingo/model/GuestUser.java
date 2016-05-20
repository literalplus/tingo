package li.l1t.tingo.model;

import li.l1t.tingo.exception.AuthException;
import org.apache.commons.lang3.Validate;

import java.security.Principal;

/**
 * Represents an authenticated guest.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 2016-05-20
 */
public class GuestUser implements User {
    public static final String NAME = "*guest";
    public static final GuestUser INSTANCE = new GuestUser();

    private GuestUser() {

    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean isRegistered() {
        return false;
    }

    public static boolean isGuest(Principal principal) {
        Validate.notNull(principal, "principal");
        return NAME.equals(principal.getName());
    }

    /**
     * Validates that a principal is not an authenticated guest, and throws an exception otherwise.
     *
     * @param principal the principal to validate
     * @throws AuthException if the principal is a guest
     */
    public static void validateNotGuest(Principal principal) {
        if (isGuest(principal)){
            throw new AuthException("Das darfst du als Gast nicht!");
        }
    }
}
