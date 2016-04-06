package li.l1t.tingo.model;

import org.hibernate.validator.constraints.Length;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.Valid;

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
    @Valid
    @Length(max = 256)
    private String text;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creator_name", updatable = false)
    private User creator;

    protected TingoField() { }

    public TingoField(Teacher teacher, String text) {
        this(0, teacher, text);
    }

    public TingoField(int id, Teacher teacher, String text) {
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

    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    @Override
    public String toString() {
        return "TingoField{" +
                "teacher=" + teacher +
                ", text='" + text + '\'' +
                '}';
    }
}
