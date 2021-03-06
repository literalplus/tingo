package li.l1t.tingo.service;

import li.l1t.tingo.exception.TeacherNotFoundException;
import li.l1t.tingo.model.Teacher;
import li.l1t.tingo.model.dto.TeacherDto;
import li.l1t.tingo.model.repo.TeacherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service handling teachers, providing a bridge between the controller and the model.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 2016-02-14
 */
@Service
public class TeacherService {
    @Autowired
    private TeacherRepository teacherRepository;

    public Iterable<Teacher> getAllTeachers() {
        return teacherRepository.findAll();
    }

    public Teacher getById(int id) {
        Teacher teacher = teacherRepository.findOne(id);
        if(teacher == null) {
            throw new TeacherNotFoundException(id);
        }
        return teacher;
    }

    public TeacherDto toDto(Teacher entity) {
        TeacherDto dto = new TeacherDto();
        dto.setId(entity.getId());
        dto.setAbbreviation(entity.getAbbreviation());
        dto.setName(entity.getName());
        return dto;
    }
}
