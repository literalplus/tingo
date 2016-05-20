package li.l1t.tingo.exception;

import org.springframework.http.HttpStatus;

/**
 * Thrown if an error occurs during authentication that needs to be returned to the user as JSON.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 2016-05-20
 */
public class AuthException extends JsonPropagatingException {
    public AuthException(String message) {
        super(message, HttpStatus.FORBIDDEN);
    }
}
