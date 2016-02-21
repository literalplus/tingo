package li.l1t.tingo.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;

/**
 * Stores configuration values for the whole application.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 2016-02-21
 */
@Service
@ConfigurationProperties(prefix = "tingo")
public class TingoConfiguration {
    private String registerSecret;

    public String getRegisterSecret() {
        return registerSecret;
    }

    public void setRegisterSecret(String registerSecret) {
        this.registerSecret = registerSecret;
    }
}
