package li.l1t.tingo.rest;

import li.l1t.tingo.model.Teacher;
import li.l1t.tingo.model.dto.TeacherDto;
import li.l1t.tingo.service.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * REST controller providing an API for getting information about teachers and modifying it.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 2016-02-14
 */
@RestController
public class TeacherController {
    private final TeacherService teacherService;

    @Autowired
    TeacherController(TeacherService teacherService) {
        this.teacherService = teacherService;
    }


    @RequestMapping("/api/teacher/list")
    public List<TeacherDto> teacherList() {
        return StreamSupport.stream(teacherService.getAllTeachers().spliterator(), false)
                .map(teacherService::toDto)
                .collect(Collectors.toList());
    }

    @RequestMapping("/api/teacher/by/id/{id}")
    public Teacher singleTeacher(@PathVariable("id") int id) {
        return teacherService.getById(id);
    }
}
