package li.l1t.tingo.model.dto;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * Data Transfer Object for an authentication request. This is a single object for both login and register requests.
 * Logins do not have a register token set.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 2016-02-21
 */
public class AuthenticationDto {
    @Valid
    @NotNull
    private String username;
    @Valid
    @NotNull
    private String password;
    private String registerToken;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRegisterToken() {
        return registerToken;
    }

    public void setRegisterToken(String registerToken) {
        this.registerToken = registerToken;
    }

    @Override
    public String toString() {
        return "AuthenticationDto{" +
                "username='" + username + '\'' +
                ", password=" + (password == null ? "null" : password.length() + " characters") +
                ", registerToken='" + registerToken + '\'' +
                '}';
    }
}
