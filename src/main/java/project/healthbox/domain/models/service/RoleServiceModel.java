package project.healthbox.domain.models.service;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class RoleServiceModel extends BaseService {
    private String authority;
    private Set<UserServiceModel> users;
    private Set<DoctorServiceModel> doctors;
}
