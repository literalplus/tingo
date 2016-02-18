package li.l1t.tingo.model.dto;

import java.util.List;

/**
 * Data Transfer Object for representing fields by teacher, with count.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 18.2.16
 */
public class FieldsDto {
    private TeacherDto teacher;
    private List<FieldDto> fields;

    public FieldsDto(TeacherDto teacher, List<FieldDto> fields) {
        this.teacher = teacher;
        this.fields = fields;
    }

    public TeacherDto getTeacher() {
        return teacher;
    }

    public void setTeacher(TeacherDto teacher) {
        this.teacher = teacher;
    }

    public List<FieldDto> getFields() {
        return fields;
    }

    public void setFields(List<FieldDto> fields) {
        this.fields = fields;
    }

    public int getCount() {
        return fields.size();
    }
}
