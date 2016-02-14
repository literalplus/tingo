package li.l1t.tingo.model.dto;

import li.l1t.tingo.exception.JsonPropagatingException;

/**
 * Data Transfer Object for soft errors.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 2016-02-14
 */
public class ErrorDto {
    private final String errorMessage;
    private final String exceptionName;

    public ErrorDto(String errorMessage) {
        this(errorMessage, null);
    }

    public ErrorDto(Exception exception) {
        this.errorMessage = exception.getMessage();
        if(exception instanceof JsonPropagatingException && exception.getCause() != null) {
            this.exceptionName = exception.getCause().getClass().getSimpleName();
        } else {
            this.exceptionName = exception.getClass().getSimpleName();
        }
    }

    public ErrorDto(String errorMessage, String exceptionName) {
        this.errorMessage = errorMessage;
        this.exceptionName = exceptionName;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public String getExceptionName() {
        return exceptionName;
    }
}
