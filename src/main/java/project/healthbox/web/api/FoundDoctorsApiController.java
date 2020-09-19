package project.healthbox.web.api;

import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import project.healthbox.domain.models.service.DoctorServiceModel;
import project.healthbox.domain.models.view.FoundDoctorViewModel;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@AllArgsConstructor
public class FoundDoctorsApiController {
    private final ModelMapper modelMapper;

    @GetMapping("/user/foundDoctors")
    public ResponseEntity<List<FoundDoctorViewModel>> getFoundDoctorsView(HttpSession httpSession, ModelAndView modelAndView) {
        List<DoctorServiceModel> allByGivenCriteria = (List<DoctorServiceModel>) httpSession.getAttribute("foundDoctors");
        List<FoundDoctorViewModel> result = allByGivenCriteria.stream()
                .map(r -> this.modelMapper.map(r, FoundDoctorViewModel.class))
                .collect(Collectors.toList());
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
