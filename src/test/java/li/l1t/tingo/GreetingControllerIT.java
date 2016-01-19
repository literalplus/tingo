package li.l1t.tingo;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.client.RestTemplate;

import java.net.URL;

import static org.hamcrest.Matchers.equalTo;

/**
 * Integration tests the greeting controller.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 2016-01-19
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = TingoStarter.class)
@WebAppConfiguration
@IntegrationTest({"server.port=0"})
public class GreetingControllerIT {

    @Value("${local.server.port}")
    private int port;
    private URL base;
    private RestTemplate template;

    @Before
    public void setUp() throws Exception {
        base = new URL("http://localhost:"+port+"/hello/World/");
        template = new TestRestTemplate();
    }

    @Test
    public void testHello() throws Exception {
        ResponseEntity<String> responseEntity = template.getForEntity(base.toString(), String.class);
        Assert.assertThat("Wrong response in greeting integration test",
                responseEntity.getBody(), equalTo("Hello, World!"));
    }
}
