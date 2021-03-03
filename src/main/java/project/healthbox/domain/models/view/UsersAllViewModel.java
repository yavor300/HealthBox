package project.healthbox.domain.models.view;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import project.healthbox.domain.models.service.RoleServiceModel;

import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class UsersAllViewModel {
    private String id;
    private String firstName;
    private String lastName;
    private String email;
    private Set<RoleServiceModel> authorities;
}
