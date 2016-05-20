package li.l1t.tingo.rest;

import li.l1t.tingo.model.GuestUser;
import li.l1t.tingo.model.Teacher;
import li.l1t.tingo.model.TingoField;
import li.l1t.tingo.model.dto.FieldDto;
import li.l1t.tingo.model.dto.FieldsDto;
import li.l1t.tingo.service.FieldService;
import li.l1t.tingo.service.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Controller prociding a REST API for Tingo fields.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 2016-02-14
 */
@RestController
public class FieldController {
    private final FieldService fieldService;
    private final TeacherService teacherService;

    @Autowired
    public FieldController(FieldService fieldService, TeacherService teacherService) {
        this.fieldService = fieldService;
        this.teacherService = teacherService;
    }

    @RequestMapping("/api/field/by/teacher/{teacherId}")
    public FieldsDto byTeacherId(@PathVariable("teacherId") int teacherId,
                                 @RequestParam(name = "limit", defaultValue = "0") int limit) {
        Teacher teacher = teacherService.getById(teacherId);

        List<FieldDto> fields = StreamSupport.stream(fieldService.getAllFieldsByTeacher(teacher).spliterator(), false)
                .map(fieldService::toDto)
                .collect(Collectors.toList());

        Collections.shuffle(fields);
        Collections.shuffle(fields); //Shuffle twice for subjective impression of being more random (to the dev)

        if(limit > 0) { //If we have a limit, apply it; Using streams for easier API (no bounds checking necessary)
            fields = fields.stream().limit(limit).collect(Collectors.toList());
        }

        return new FieldsDto(teacherService.toDto(teacher), fields);
    }

    @RequestMapping(value = "/api/field/save", method = RequestMethod.POST)
    public FieldDto createField(@RequestBody FieldDto field, Principal user) {
        GuestUser.validateNotGuest(user);
        return fieldService.toDto(fieldService.save(field, user));
    }

    @RequestMapping(value = "/api/field/delete", method = RequestMethod.POST)
    public FieldDto deleteFieldById(@RequestBody FieldDto fieldDto, Principal user) {
        GuestUser.validateNotGuest(user);
        TingoField field = fieldService.toEntity(fieldDto);
        fieldService.delete(field);
        return fieldService.toDto(field);
    }
}
