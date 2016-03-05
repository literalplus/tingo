package li.l1t.tingo.exception;

import io.jsonwebtoken.SignatureException;
import li.l1t.tingo.model.dto.ErrorDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * Controller advice for handling some exceptions.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 2016-02-14
 */
@ControllerAdvice
public class ExceptionHandlerControllerAdvice {
    @ExceptionHandler(JsonPropagatingException.class)
    public ResponseEntity<ErrorDto> handleJsonPropagatingException(JsonPropagatingException exception) {
        return new ResponseEntity<>(new ErrorDto(exception), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(SignatureException.class)
    public ResponseEntity<ErrorDto> handleSignatureException(SignatureException exception) {
        return new ResponseEntity<>(new ErrorDto(exception), HttpStatus.UNAUTHORIZED);
    }
}
