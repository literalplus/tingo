package li.l1t.tingo.service;

import li.l1t.tingo.model.Teacher;
import li.l1t.tingo.model.TingoField;
import li.l1t.tingo.model.dto.FieldDto;
import li.l1t.tingo.model.repo.FieldRepository;
import org.dozer.DozerBeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service handling Tingo fields, specifically finding fields by teacher,
 * adding, creating and deleting fields.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 2016-02-14
 */
@Service
public class FieldService {
    private final FieldRepository fieldRepository;
    private final DozerBeanMapper dozerBeanMapper;

    @Autowired
    public FieldService(FieldRepository fieldRepository, DozerBeanMapper dozerBeanMapper) {
        this.fieldRepository = fieldRepository;
        this.dozerBeanMapper = dozerBeanMapper;
    }

    public List<TingoField> getAllFieldsByTeacher(Teacher teacher) {
        return fieldRepository.findAllByTeacher(teacher);
    }

    public FieldDto toDto(TingoField field) {
        return dozerBeanMapper.map(field, FieldDto.class);
    }
}
