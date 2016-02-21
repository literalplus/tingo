package li.l1t.tingo.model.dto;

/**
 * Data Transfer Object for a registration request.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 2016-02-21
 */
public class RegistrationRequestDto {
    private String username;
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
        return "RegistrationRequestDto{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", registerToken='" + registerToken + '\'' +
                '}';
    }
}
