package project.healthbox.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import project.healthbox.domain.entities.Specialty;
import project.healthbox.domain.models.service.SpecialtyServiceModel;
import project.healthbox.repostory.SpecialtyRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SpecialtyServiceImpl implements SpecialtyService {
    private final SpecialtyRepository specialtyRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public SpecialtyServiceImpl(SpecialtyRepository specialtyRepository, ModelMapper modelMapper) {
        this.specialtyRepository = specialtyRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<SpecialtyServiceModel> getAll() {
        return this.specialtyRepository.findAll().stream()
                .map(s -> this.modelMapper.map(s, SpecialtyServiceModel.class))
                .collect(Collectors.toList());
    }

    @Override
    public SpecialtyServiceModel findByName(String name) {
        Specialty specialty = this.specialtyRepository.findByName(name);
        if (specialty != null) {
            return this.modelMapper.map(specialty, SpecialtyServiceModel.class);
        } else {
            return null;
        }
    }

    @Override
    public String getIdBySpecialtyName(String name) {
        SpecialtyServiceModel specialtyServiceModel = this.findByName(name);
        if (specialtyServiceModel == null) {
            return "";
        }
        return specialtyServiceModel.getId();
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
