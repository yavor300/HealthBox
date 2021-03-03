package project.healthbox.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import project.healthbox.domain.entities.Doctor;
import project.healthbox.domain.entities.User;
import project.healthbox.repostory.DoctorRepository;
import project.healthbox.repostory.UserRepository;

import java.util.Objects;

@Service
@AllArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;
    private final DoctorRepository doctorRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = this.userRepository
                .findByEmail(email)
                .orElse(null);

        Doctor doctor = this.doctorRepository
                .findByEmail(email)
                .orElse(null);

        if (user == null && doctor == null) {
            throw new UsernameNotFoundException("User not found!");
        }

        return Objects.requireNonNullElse(doctor, user);
    }
}
