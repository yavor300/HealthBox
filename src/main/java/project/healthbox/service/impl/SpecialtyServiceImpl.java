package project.healthbox.service.impl;

import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import project.healthbox.domain.entities.Specialty;
import project.healthbox.domain.models.service.SpecialtyServiceModel;
import project.healthbox.error.SpecialtyNotFoundException;
import project.healthbox.repostory.SpecialtyRepository;
import project.healthbox.service.SpecialtyService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class SpecialtyServiceImpl implements SpecialtyService {
    private final SpecialtyRepository specialtyRepository;
    private final ModelMapper modelMapper;

    @Override
    public List<SpecialtyServiceModel> getAll() {
        return specialtyRepository.findAll().stream()
                .map(specialty -> modelMapper.map(specialty, SpecialtyServiceModel.class))
                .collect(Collectors.toList());
    }

    @Override
    public SpecialtyServiceModel getByName(String name) {
        return specialtyRepository.findByName(name)
                .map(specialty -> modelMapper.map(specialty, SpecialtyServiceModel.class))
                .orElseThrow(() -> new SpecialtyNotFoundException("Invalid specialty name!"));
    }

    @Override
    public SpecialtyServiceModel getById(String id) {
        return this.modelMapper.map(this.specialtyRepository.getById(id), SpecialtyServiceModel.class);
    }

    @Override
    public void deleteSpecialty(String id) {
        Specialty specialty = this.specialtyRepository.getById(id);
        this.specialtyRepository.delete(specialty);
    }

    @Override
    public SpecialtyServiceModel createSpecialty(String name) {
        return this.modelMapper.map(this.specialtyRepository.saveAndFlush(new Specialty(name)), SpecialtyServiceModel.class);
    }
}
