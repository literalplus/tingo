package li.l1t.tingo.model.repo;

import li.l1t.tingo.model.UserAuthority;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repository for authorities.
 *
 * @since 2016-02-22
 */

public interface AuthorityRepository extends JpaRepository<UserAuthority, Integer> {
    List<UserAuthority> getAllByName(String username);
}
