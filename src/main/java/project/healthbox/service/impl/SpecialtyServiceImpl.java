package project.healthbox.service.impl;

import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import project.healthbox.domain.entities.Specialty;
import project.healthbox.domain.models.service.SpecialtyServiceModel;
import project.healthbox.error.ObjectAlreadyExistsException;
import project.healthbox.error.ObjectNotFoundException;
import project.healthbox.repostory.SpecialtyRepository;
import project.healthbox.service.SpecialtyService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SpecialtyServiceImpl implements SpecialtyService {
    private final SpecialtyRepository specialtyRepository;
    private final ModelMapper modelMapper;
    private final Resource specilatiesFile;
    private final Gson gson;

    public SpecialtyServiceImpl(SpecialtyRepository specialtyRepository, ModelMapper modelMapper, @Value("classpath:init/specialties.json") Resource specilatiesFile, Gson gson) {
        this.specialtyRepository = specialtyRepository;
        this.modelMapper = modelMapper;
        this.specilatiesFile = specilatiesFile;
        this.gson = gson;
    }

    @Override
    public void seedSpecialties() {
        if (specialtyRepository.count() == 0) {
            try {
                Specialty[] specialties =
                        gson.fromJson(Files.readString(Path.of(specilatiesFile.getURI())), Specialty[].class);

                Arrays.stream(specialties).
                        forEach(specialtyRepository::save);
            } catch (IOException e) {
                throw new IllegalStateException("Cannot seed specialties...");
            }
        }
    }

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