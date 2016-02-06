package li.l1t.tingo;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 2016-02-06
 */
@Controller
public class LandingController {

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String main(@RequestParam(value = "name", required = false, defaultValue = "World") String name, Model model) {
        model.addAttribute("name", name);
        return "main";
    }
}
