package li.l1t.tingo.model.repo;

import li.l1t.tingo.model.RegisteredUser;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository for user data.
 *
 * @since 2016-02-21
 */
@Repository
public interface UserRepository extends CrudRepository<RegisteredUser, String> {
    Optional<RegisteredUser> findByName(String name);
}
