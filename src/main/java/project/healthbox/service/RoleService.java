package project.healthbox.service;

import project.healthbox.domain.models.service.RoleServiceModel;

import java.util.Set;

public interface RoleService {
    void seedRolesInDb();

    RoleServiceModel getByAuthority(String authority);

    Set<RoleServiceModel> setRolesForRootUser();
}
