package li.l1t.tingo.model.repo;

import li.l1t.tingo.model.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Repository for teacher data.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 2016-02-14
 */
@Repository
public interface TeacherRepository extends JpaRepository<Teacher, Integer> {

    Teacher findByAbbreviation(String abbreviation);

    Teacher findByName(String name);

    @Query(value = "SELECT * FROM tingo_teacher WHERE name LIKE CONCAT('%', :nameSearch, '%')",
            nativeQuery = true)
    Teacher findByNameFuzzy(String nameSearch);
}
