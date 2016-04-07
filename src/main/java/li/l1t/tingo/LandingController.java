package li.l1t.tingo;

import li.l1t.tingo.config.TingoConfiguration;
import li.l1t.tingo.model.Teacher;
import li.l1t.tingo.service.FieldService;
import li.l1t.tingo.service.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;

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
        return "main";
    }

    @RequestMapping(value = "/secure/print/teacher/by/id/{id}", method = RequestMethod.GET)
    public String teacherPrint(Model model, @PathVariable("id") int id) {
        Teacher teacher = teacherService.getById(id);
        model.addAttribute("teacher", teacher);
        model.addAttribute("fields", fieldService.getAllFieldsByTeacher(teacher));
        return "teacher-print";
    }

    @RequestMapping(value = "/error", method = RequestMethod.GET)
    public String error(HttpServletRequest request, Model model) {
        Object errCodeObj = request.getAttribute("javax.servlet.error.status_code");
        int errorCode = errCodeObj == null ? 0 : (int) errCodeObj;
        model.addAttribute("errorCode", errCodeObj);
        model.addAttribute("errorType", request.getAttribute("javax.servlet.error.exception"));

        switch(errorCode) {
            case 401:
                return "error/401.html";
            case 0:
            default:
                return "error/generic.html";
        }
    }
}
