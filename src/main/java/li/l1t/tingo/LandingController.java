package li.l1t.tingo;

import li.l1t.tingo.config.TingoConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

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

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String main(Model model) {
        model.addAttribute("footerText", configuration.getFooterText());
        return "main";
    }
}
