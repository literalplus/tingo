package li.l1t.tingo.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Represents a field in a Tingo bingo field, representing something a teacher might do in a lesson.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 2016-02-14
 */
@Entity
@Table(name = "tingo_field")
public class TingoField extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "teacher_id", updatable = false, nullable = false)
    private Teacher teacher;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String text;

    protected TingoField() { }

    public TingoField(Teacher teacher, String text) {
        this.teacher = teacher;
        this.text = text;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return "TingoField{" +
                "teacher=" + teacher +
                ", text='" + text + '\'' +
                '}';
    }
}
