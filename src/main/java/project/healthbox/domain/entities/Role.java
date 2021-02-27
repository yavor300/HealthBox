package project.healthbox.domain.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.util.Set;

@Entity
@Table(name = "roles")
@NoArgsConstructor
@Getter
@Setter
public class Role extends BaseEntity implements GrantedAuthority {

    @Column(name = "authority", unique = true)
    private String authority;

    @ManyToMany(mappedBy = "authorities")
    private Set<User> users;

    @ManyToMany(mappedBy = "authorities")
    private Set<User> doctors;

    public Role(String authority) {
        this.authority = authority;
    }
}