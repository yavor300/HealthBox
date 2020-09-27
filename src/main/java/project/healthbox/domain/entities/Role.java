package project.healthbox.domain.entities;

import org.springframework.security.core.GrantedAuthority;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.util.Set;

@Entity
@Table(name = "roles")
public class Role extends BaseEntity implements GrantedAuthority {

    @Column(name = "authority", unique = true)
    private String authority;
    @ManyToMany(mappedBy = "authorities")
    private Set<User> users;
    @ManyToMany(mappedBy = "authorities")
    private Set<User> doctors;

    public Role() {
    }

    public Role(String authority) {
        this.authority = authority;
    }

    @Override
    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    public Set<User> getDoctors() {
        return doctors;
    }

    public void setDoctors(Set<User> doctors) {
        this.doctors = doctors;
    }
}