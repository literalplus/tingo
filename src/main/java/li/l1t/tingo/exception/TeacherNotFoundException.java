package li.l1t.tingo.exception;

/**
 * Thrown if a teacher with an unknown id is requested.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 2016-03-06
 */
public class TeacherNotFoundException extends JsonPropagatingException {
    public TeacherNotFoundException(long requestedId) {
        super("Unknown teacher with id " + requestedId);
    }
}
