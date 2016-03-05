package li.l1t.tingo.misc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.security.SecureRandom;

/**
 * Generates a single random string at application startup and returns it afterwards.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 2016-03-05
 */
@Service
public class PersistentTokenGenerator {
    private static final Logger LOGGER = LoggerFactory.getLogger(PersistentTokenGenerator.class);
    private final String token;

    @Autowired
    public PersistentTokenGenerator(@Value("#{new java.security.SecureRandom()}") SecureRandom random) {
        token = new BigInteger(130, random).toString(32); //26 characters: 130 bits / (log_2(32) bits/char) = 26 chars
        LOGGER.info("Persistent token generated: " + token);
    }

    /**
     * @return the persistent token generated at startup
     */
    public String getToken() {
        return token;
    }
}
