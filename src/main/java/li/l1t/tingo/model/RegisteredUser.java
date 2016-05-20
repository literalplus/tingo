package li.l1t.tingo.model;

import org.hibernate.validator.constraints.Length;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.Valid;

/**
 * Represents a user.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 2016-02-21
 */
@Entity
@Table(name = "tingo_user")
public class RegisteredUser implements User {
    @Valid
    @Length(min = 2, max = 20)
    @Column(name = "username")
    @Id
    private String name;

    @Valid
    @Length(min = 60, max = 60)
    private String password;

    private boolean enabled;

    public RegisteredUser() {}

    public RegisteredUser(String name, String password, boolean enabled) {
        this.name = name;
        this.password = password;
        this.enabled = enabled;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public boolean isRegistered() {
        return true;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
