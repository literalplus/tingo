package li.l1t.tingo;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * Controls displaying of error messages.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 7.4.16
 */
@Controller
public class TingoErrorController implements ErrorController {
    @RequestMapping(value = "/error")
    public String errorHtml(HttpServletRequest request, Model model) {
        Object errCodeObj = request.getAttribute("javax.servlet.error.status_code");
        int errorCode = errCodeObj == null ? 0 : (int) errCodeObj;
        model.addAttribute("errorCode", errCodeObj);
        model.addAttribute("errorType", request.getAttribute("javax.servlet.error.exception"));

        return switch (errorCode) {
            case 401 -> "error/401";
            default -> "error/generic";
        };
    }
}
