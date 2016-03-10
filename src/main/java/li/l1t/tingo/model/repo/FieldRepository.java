package li.l1t.tingo.model.repo;

import li.l1t.tingo.model.Teacher;
import li.l1t.tingo.model.TingoField;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Repository for Tingo fields.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 2016-02-14
 */
@org.springframework.stereotype.Repository
public interface FieldRepository extends CrudRepository<TingoField, Integer> {
    List<TingoField> findAllByTeacher(Teacher teacher);
}
