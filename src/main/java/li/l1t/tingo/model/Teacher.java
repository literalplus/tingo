package li.l1t.tingo.model;

import org.hibernate.validator.constraints.Length;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.Valid;

/**
 * Represents a teacher.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 2016-02-14
 */
@Entity
@Table(name = "tingo_teacher")
public class Teacher extends BaseEntity {
    @Valid
    @Length(min = 4, max = 4)
    @Column(name = "abbr", columnDefinition = "VARCHAR(4)")
    private String abbreviation;
    @Valid
    @Length(max = 100)
    private String name;

    public String getAbbreviation() {
        return abbreviation;
    }

    public void setAbbreviation(String abbreviation) {
        this.abbreviation = abbreviation;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Teacher{" +
                "abbreviation='" + abbreviation + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
