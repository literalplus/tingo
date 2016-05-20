package li.l1t.tingo;

import li.l1t.tingo.config.TingoConfiguration;
import li.l1t.tingo.model.Teacher;
import li.l1t.tingo.model.TingoField;
import li.l1t.tingo.service.FieldService;
import li.l1t.tingo.service.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Controller for serving Tingo's main template, aka the AngularJS client.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 2016-02-06
 */
@Controller
public class LandingController {
    @Autowired
    private TingoConfiguration configuration;
    @Autowired
    private TeacherService teacherService;
    @Autowired
    private FieldService fieldService;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String main(Model model) {
        model.addAttribute("footerText", configuration.getFooterText());
        model.addAttribute("navbarLinks", configuration.getNavbarLinks());
        return "main";
    }

    @RequestMapping(value = "/secure/print/teacher/by/id/{id}/pages/{boardCount}", method = RequestMethod.GET)
    public String teacherPrint(Model model, @PathVariable int id,
                               @PathVariable @Valid @Max(142) @Min(1) int boardCount) {
        Teacher teacher = teacherService.getById(id);
        model.addAttribute("teacher", teacher);
        List<TingoField> fields = fieldService.getAllFieldsByTeacher(teacher);
        List<List<TingoField>> boards = new ArrayList<>(boardCount);
        for(int i = 0; i < boardCount; i++) {
            boards.add(prepareFields(fields));
        }
        model.addAttribute("boards", boards);
        return "teacher-print";
    }

    private List<TingoField> prepareFields(List<TingoField> fields) {
        List<TingoField> newFields = new ArrayList<>(fields);
        Collections.shuffle(newFields);
        Collections.shuffle(newFields); //subjective feeling of safety
        return newFields.size() < 25 ? newFields : newFields.subList(0, 25);
    }
}
