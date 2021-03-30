package project.healthbox.init;

import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import project.healthbox.service.CityService;
import project.healthbox.service.RoleService;
import project.healthbox.service.SpecialtyService;
import project.healthbox.service.UserService;

@Component
@AllArgsConstructor
public class ApplicationDataInitialization implements CommandLineRunner {
    private final CityService cityService;
    private final SpecialtyService specialtyService;
    private final RoleService roleService;
    private final UserService userService;


    @Override
    public void run(String... args) throws Exception {
        cityService.seedCities();
        specialtyService.seedSpecialties();
        roleService.seedRoles();
        userService.seedRootUser();
    }
}
