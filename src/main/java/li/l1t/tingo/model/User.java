package li.l1t.tingo.model;

/**
 * Represents a user.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 2016-05-20
 */
public interface User {
    String getName();

    String getPassword();

    boolean isEnabled();

    boolean isRegistered();
}
