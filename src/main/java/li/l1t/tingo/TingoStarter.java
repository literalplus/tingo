package li.l1t.tingo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

/**
 * The Spring starter class for Tingo.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 2016-01-15
 */
@SpringBootApplication
@EnableConfigurationProperties
public class TingoStarter {
    public static void main(String[] args) {
        SpringApplication.run(TingoStarter.class, args).getEnvironment();
    }
}
