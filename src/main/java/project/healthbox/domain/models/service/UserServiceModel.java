package project.healthbox.domain.models.service;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserServiceModel extends BaseService {
    private String firstName;
    private String lastName;
    private String title;
    private String email;
    private String password;
}
