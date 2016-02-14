package li.l1t.tingo.exception;

/**
 * Exception class for propagating an error message that is returned
 * as JSON object to the client.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 2016-02-14
 */
public class JsonPropagatingException extends RuntimeException {
    public JsonPropagatingException(String message) {
        super(message);
    }

    public JsonPropagatingException(String message, Throwable cause) {
        super(message, cause);
    }

    public JsonPropagatingException(Throwable cause) {
        super(cause);
    }
}
