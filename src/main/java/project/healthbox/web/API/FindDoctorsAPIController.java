package project.healthbox.web.API;

import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import project.healthbox.domain.models.service.DoctorServiceModel;
import project.healthbox.domain.models.view.DoctorFoundAPIViewModel;
import project.healthbox.service.DoctorService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@AllArgsConstructor
public class FindDoctorsAPIController {
    private final DoctorService doctorService;
    private final ModelMapper modelMapper;

    @GetMapping("/findDoctors/specialty_id={specialtyId}&city_id={cityId}&first_name={firstName}&last_name={lastName}")
    public ResponseEntity<List<DoctorFoundAPIViewModel>> findDoctors(@PathVariable(required = false) String specialtyId,
                                                                     @PathVariable(required = false) String cityId,
                                                                     @PathVariable(required = false) String firstName,
                                                                     @PathVariable(required = false) String lastName) {

        List<DoctorServiceModel> found = doctorService.getAllByGivenCriteria(specialtyId, cityId, firstName, lastName);

        List<DoctorFoundAPIViewModel> result = found.stream().map(doctorServiceModel -> modelMapper.map(doctorServiceModel, DoctorFoundAPIViewModel.class))
                .collect(Collectors.toList());

        if (result.isEmpty()) {
            return new ResponseEntity<>(result, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}