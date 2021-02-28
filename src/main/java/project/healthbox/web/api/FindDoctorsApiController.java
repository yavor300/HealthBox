package project.healthbox.web.api;

import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import project.healthbox.domain.entities.Doctor;
import project.healthbox.domain.models.view.FoundDoctorViewModel;
import project.healthbox.repostory.DoctorRepository;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@AllArgsConstructor
public class FindDoctorsApiController {
    private final ModelMapper modelMapper;
    private final DoctorRepository doctorRepository;


    @GetMapping("/findDoctors/specialty_id={specialtyId}&city_id={cityId}&first_name={firstName}&last_name={lastName}")
    public ResponseEntity<List<FoundDoctorViewModel>> foundDoctors(@PathVariable(required = false) String specialtyId, @PathVariable(required = false) String cityId, @PathVariable(required = false) String firstName,
                                                                   @PathVariable(required = false) String lastName) {

        List<Doctor> allByGivenCriteria = doctorRepository.findAllBySpecialtyIdAndLocationIdAndFirstNameAndLastName(specialtyId, cityId, firstName,
                lastName);

        List<FoundDoctorViewModel> result = allByGivenCriteria.stream()
                .map(r -> this.modelMapper.map(r, FoundDoctorViewModel.class))
                .collect(Collectors.toList());

        return new ResponseEntity<>(result, HttpStatus.FOUND);
    }
}
