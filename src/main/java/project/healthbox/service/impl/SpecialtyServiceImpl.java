package project.healthbox.service.impl;

import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import project.healthbox.domain.entities.Specialty;
import project.healthbox.domain.models.service.SpecialtyServiceModel;
import project.healthbox.error.ObjectAlreadyExistsException;
import project.healthbox.error.ObjectNotFoundException;
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
                .orElseThrow(() -> new ObjectNotFoundException("Invalid specialty name!"));
    }

    @Override
    public SpecialtyServiceModel getById(String id) {
        return specialtyRepository.findById(id)
                .map(specialty -> modelMapper.map(specialty, SpecialtyServiceModel.class))
                .orElseThrow(() -> new ObjectNotFoundException("Invalid specialty identifier!"));
    }

    @Override
    public void deleteSpecialty(String id) {
        Specialty specialty = specialtyRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Invalid specialty identifier!"));
        specialtyRepository.delete(specialty);
    }

    @Override
    public SpecialtyServiceModel createSpecialty(String name) {
        if (specialtyRepository.findByName(name).isPresent()) {
            throw new ObjectAlreadyExistsException("Specialty with that name is already present in the database!");
        }
        return modelMapper.map(specialtyRepository.saveAndFlush(new Specialty(name)), SpecialtyServiceModel.class);
    }
}