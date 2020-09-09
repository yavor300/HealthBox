package project.healthbox.domain.models.service;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserLoginServiceModel {
    private String id;
    private String firstName;
    private String lastName;
    private String title;
}
