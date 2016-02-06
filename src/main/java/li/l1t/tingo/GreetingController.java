package li.l1t.tingo;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Greets using accessing the /hello/ API.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 2016-01-15
 */
@RestController
public class GreetingController {
    private final AtomicLong counter = new AtomicLong();

    @RequestMapping("/hello/{name}")
    public Greeting hello(@PathVariable String name) {
        return new Greeting("Hello, " + name + "!", counter.incrementAndGet());
    }

    public static final class Greeting {
        private final String greeting;
        private final long id;

        public Greeting(String greeting, long id) {
            this.greeting = greeting;
            this.id = id;
        }

        public String getGreeting() {
            return greeting;
        }

        public long getId() {
            return id;
        }
    }
}
