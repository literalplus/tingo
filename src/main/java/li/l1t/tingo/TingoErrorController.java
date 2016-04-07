package li.l1t.tingo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ErrorAttributes;
import org.springframework.boot.autoconfigure.web.ErrorController;
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
    private static final String ERROR_PATH = "/error";

    @Autowired
    private ErrorAttributes errorAttributes;

    @Override
    public String getErrorPath() {
        return ERROR_PATH;
    }

    @RequestMapping(value = ERROR_PATH)
    public String errorHtml(HttpServletRequest request, Model model) {
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
