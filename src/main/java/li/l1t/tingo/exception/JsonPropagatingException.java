package li.l1t.tingo.exception;

import org.apache.commons.lang3.Validate;
import org.springframework.http.HttpStatus;

/**
 * Exception class for propagating an error message that is returned
 * as JSON object to the client.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 2016-02-14
 */
public class JsonPropagatingException extends RuntimeException {
    private HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;

    public JsonPropagatingException(String message) {
        super(message);
    }

    public JsonPropagatingException(String message, Throwable cause) {
        super(message, cause);
    }

    public JsonPropagatingException(Throwable cause) {
        super(cause);
    }

    public JsonPropagatingException(String message, HttpStatus status) {
        this(message, null, status);
    }

    public JsonPropagatingException(String message, Throwable cause, HttpStatus status) {
        super(message, cause);
        Validate.notNull(status, "status");
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public void setStatus(HttpStatus status) {
        Validate.notNull(status, "status");
        this.status = status;
    }
}

