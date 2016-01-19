package li.l1t.tingo;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Greets using accessing the /hello/ API.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 2016-01-15
 */
@RestController
public class GreetingController {

    @RequestMapping("/hello/{name}")
    public String hello(@PathVariable String name) {
        return "Hello, " + name + "!";
    }
}
