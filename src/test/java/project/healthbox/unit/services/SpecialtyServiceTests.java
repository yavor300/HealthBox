package project.healthbox.unit.services;

import com.google.gson.Gson;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.core.io.Resource;
import project.healthbox.domain.entities.Specialty;
import project.healthbox.domain.models.service.SpecialtyServiceModel;
import project.healthbox.error.ObjectAlreadyExistsException;
import project.healthbox.error.ObjectNotFoundException;
import project.healthbox.repostory.SpecialtyRepository;
import project.healthbox.service.SpecialtyService;
import project.healthbox.service.impl.SpecialtyServiceImpl;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;


public class SpecialtyServiceTests {
    private static final String ID = "UUID";
    private static final String NAME = "NAME";

    private SpecialtyService specialtyService;
    private SpecialtyRepository mockSpecialtyRepository;

    private Specialty specialty;

    @Before
    public void init() {
        mockSpecialtyRepository = Mockito.mock(SpecialtyRepository.class);
        specialtyService = new SpecialtyServiceImpl(mockSpecialtyRepository, new ModelMapper(), Mockito.mock(Resource.class), new Gson());

        specialty = new Specialty();
        specialty.setId(ID);
        specialty.setName(NAME);

    }

    @Test
    public void getAll_Should_Return_CollectionOfSpecialties() {
        Mockito.when(mockSpecialtyRepository.findAll())
                .thenReturn(List.of(specialty));

        List<SpecialtyServiceModel> specialtyServiceAll = specialtyService.getAll();
        SpecialtyServiceModel specialtyServiceModel = specialtyServiceAll.get(0);

        assertEquals(1, specialtyServiceAll.size());
        assertEquals(specialty.getName(), specialtyServiceModel.getName());
        assertEquals(specialty.getId(), specialtyServiceModel.getId());
    }

    @Test
    public void getByName_Should_Return_CorrectSpecialty() {
        Mockito.when(mockSpecialtyRepository.findByName(NAME))
                .thenReturn(Optional.of(specialty));

        SpecialtyServiceModel specialtyServiceByName = specialtyService.getByName(NAME);

        assertEquals(specialty.getId(), specialtyServiceByName.getId());
        assertEquals(specialty.getName(), specialtyServiceByName.getName());
    }

    @Test(expected = ObjectNotFoundException.class)
    public void getByName_Should_ThrowAnException() {
        Mockito.when(mockSpecialtyRepository.findByName(NAME))
                .thenReturn(Optional.empty());

        specialtyService.getByName(NAME);
    }

    @Test
    public void getById_Should_Return_CorrectSpecialty() {
        Mockito.when(mockSpecialtyRepository.findById(ID))
                .thenReturn(Optional.of(specialty));

        SpecialtyServiceModel specialtyServiceById = specialtyService.getById(ID);

        assertEquals(specialty.getId(), specialtyServiceById.getId());
        assertEquals(specialty.getName(), specialtyServiceById.getName());
    }

    @Test(expected = ObjectNotFoundException.class)
    public void getById_Should_ThrowAnException_If_SpecialtyDoesNotExist() {
        Mockito.when(mockSpecialtyRepository.findById(ID))
                .thenReturn(Optional.empty());

        specialtyService.getById(ID);
    }

    @Test
    public void deleteSpecialty_Should_ExecuteCorrectly() {
        Mockito.when(mockSpecialtyRepository.findById(ID))
                .thenReturn(Optional.of(specialty));

        specialtyService.deleteSpecialty(ID);

        Mockito.verify(mockSpecialtyRepository, times(1)).delete(specialty);
    }

    @Test(expected = ObjectNotFoundException.class)
    public void deleteSpecialty_Should_ThrownAnException() {
        Mockito.when(mockSpecialtyRepository.findById(ID))
                .thenReturn(Optional.empty());

        specialtyService.deleteSpecialty(ID);
    }

    @Test
    public void createSpecialty_Should_Save_And_Return_TheCity() {
        Mockito.when(mockSpecialtyRepository.findByName(NAME))
                .thenReturn(Optional.empty());

        Mockito.when(mockSpecialtyRepository.saveAndFlush(any(Specialty.class)))
                .thenReturn(specialty);

        SpecialtyServiceModel specialtyServiceModel = specialtyService.createSpecialty(NAME);

        Assert.assertEquals(specialty.getId(), specialtyServiceModel.getId());
        Assert.assertEquals(specialty.getName(), specialtyServiceModel.getName());
    }

    @Test(expected = ObjectAlreadyExistsException.class)
    public void createSpecialty_Should_ThrownAnException() {
        Mockito.when(mockSpecialtyRepository.findByName(NAME))
                .thenReturn(Optional.of(specialty));

        specialtyService.createSpecialty(NAME);
    }
}