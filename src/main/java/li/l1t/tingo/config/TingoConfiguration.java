package li.l1t.tingo.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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
    private String footerText;
    private List<NavbarLink> navbarLinks;
    private String guestCode;

    public String getRegisterSecret() {
        return registerSecret;
    }

    public void setRegisterSecret(String registerSecret) {
        this.registerSecret = registerSecret;
    }

    public String getFooterText() {
        return footerText;
    }

    public void setFooterText(String footerText) {
        this.footerText = footerText;
    }

    public List<NavbarLink> getNavbarLinks() {
        if(navbarLinks == null) {
            navbarLinks = new ArrayList<>();
        }
        return navbarLinks;
    }

    public void setNavbarLinks(List<NavbarLink> navbarLinks) {
        this.navbarLinks = navbarLinks;
    }

    public String getGuestCode() {
        return guestCode;
    }

    public void setGuestCode(String guestCode) {
        this.guestCode = guestCode;
    }
}
