package project.healthbox.integration.controllers;

import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import project.healthbox.domain.entities.enums.TitleEnum;
import project.healthbox.repostory.UserRepository;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
@RunWith(SpringRunner.class)
public class UserControllerTests {

    private static final String USER_CONTROLLER_PREFIX = "/user";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void register_Should_ReturnCorrectStatusCode_AndView() throws Exception {
        mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/user/register"))
                .andExpect(status().isOk())
                .andExpect(view().name("user/register"));
    }

    @Test
    public void register_Should_RegisterUserSuccessfully() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(USER_CONTROLLER_PREFIX + "/register")
                .param("firstName", "First")
                .param("lastName", "Last").
                        param("title", TitleEnum.PATIENT.name()).
                        param("email", "test@patient.com").
                        param("password", "h[v`8r2TA')!-Ln3").
                        param("confirmPassword", "h[v`8r2TA')!-Ln3").
                        with(csrf())).
                andExpect(status().is3xxRedirection());

        Assertions.assertEquals(1, userRepository.count());
        Assertions.assertTrue(userRepository.existsByEmail("test@patient.com"));
    }
}