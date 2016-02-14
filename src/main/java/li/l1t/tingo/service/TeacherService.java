package li.l1t.tingo.service;

import li.l1t.tingo.model.Teacher;
import li.l1t.tingo.model.dto.TeacherDto;
import li.l1t.tingo.model.repo.TeacherRepository;
import org.dozer.DozerBeanMapper;
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
    private final DozerBeanMapper dozerBeanMapper;
    private final TeacherRepository teacherRepository;

    @Autowired
    public TeacherService(DozerBeanMapper dozerBeanMapper, TeacherRepository teacherRepository) {
        this.dozerBeanMapper = dozerBeanMapper;
        this.teacherRepository = teacherRepository;
    }

    public Iterable<Teacher> getAllTeachers() {
        return teacherRepository.findAll();
    }

    public Teacher getById(int id) {
        return teacherRepository.findOne(id);
    }

    public TeacherDto toDto(Teacher entity) {
        return dozerBeanMapper.map(entity, TeacherDto.class);
    }
}
