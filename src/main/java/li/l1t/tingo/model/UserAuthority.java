package li.l1t.tingo.model;

import org.hibernate.validator.constraints.Length;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.Valid;

/**
 * Represents an authority assigned to a user. Necessary because Spring doesn't accept users without authorities as
 * existing for some reason.
 *
 * @since 2016-02-22
 */
@Entity
@Table(name = "tingo_authority")
public class UserAuthority {
    @Id
    private int id;

    @Valid
    @Length(max = 50, min = 3)
    @Column(name = "username")
    private String name;

    @Column
    private String authority;

    public UserAuthority() {
    }

    public UserAuthority(String name, String authority) {
        this.name = name;
        this.authority = authority;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
