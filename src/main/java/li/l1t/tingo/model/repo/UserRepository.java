package li.l1t.tingo.model.repo;

import li.l1t.tingo.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for user data.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 2016-02-21
 */
@Repository
public interface UserRepository extends CrudRepository<User, String> {
    User findByName(String name);
}
