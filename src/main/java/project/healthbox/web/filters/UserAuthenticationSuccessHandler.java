package project.healthbox.web.filters;

import org.springframework.data.annotation.Transient;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import project.healthbox.domain.models.service.DoctorServiceModel;
import project.healthbox.domain.models.service.UserServiceModel;
import project.healthbox.service.AuthenticatedUserService;
import project.healthbox.service.DoctorService;
import project.healthbox.service.UserService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.io.IOException;

@Component
public class UserAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    private final AuthenticatedUserService authenticatedUserService;
    private final UserService userService;
    private final DoctorService doctorService;
    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    public UserAuthenticationSuccessHandler(
            UserService userService,
            DoctorService doctorService, AuthenticatedUserService authenticatedUserService) {
        super();
        this.userService = userService;
        this.authenticatedUserService = authenticatedUserService;
        this.doctorService = doctorService;
    }

    @Override
    @Transactional
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, org.springframework.security.core.Authentication authentication) throws IOException, ServletException {
        String email = authenticatedUserService.getUsername(); //email maybe..
        System.out.println("Test");

            UserServiceModel user = userService.getByEmail(email);
            DoctorServiceModel doctor = doctorService.getByEmail(email);

            if (user == null && doctor == null) {
                redirectStrategy.sendRedirect(httpServletRequest, httpServletResponse, "/user/login");
            }

            if (doctor == null) {
                redirectStrategy.sendRedirect(httpServletRequest, httpServletResponse, "/home");
            } else {
                if (doctor.getBiography() == null || doctor.getBiography().trim().isEmpty()) {
                    //not completed
                    redirectStrategy.sendRedirect(httpServletRequest, httpServletResponse, "/doctor/complete");
                } else {
                    //is completed
                    redirectStrategy.sendRedirect(httpServletRequest, httpServletResponse, "/doctor/dashboard");

                }
            }
    }
}
