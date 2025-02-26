package com.example.drone.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.drone.dto.CheckDroneDto;
import com.example.drone.dto.DroneAvailabilityDto;
import com.example.drone.dto.DroneInformationDto;
import com.example.drone.dto.LoadMedicationDto;
import com.example.drone.dto.NewDroneDto;
import com.example.drone.entity.Drone;
import com.example.drone.entity.Medication;
import com.example.drone.repository.DroneRepository;
import com.example.drone.repository.MedicationRepository;

@ExtendWith(MockitoExtension.class)
public class DroneServiceTest {
    
    @Mock
    private DroneRepository droneRepository;

    @Mock
    private MedicationRepository medicationRepository;

    @InjectMocks
    private DroneService droneService;

    private NewDroneDto mockDroneDto;
    private Drone mockSavedDrone;
    private LoadMedicationDto mockLoadMedicationDto;
    private List<Medication> mockMedicationList;
    private Medication mockMedication;
    private Medication mockSavedMedication;
    private Drone mockLoadedDrone;
    private CheckDroneDto mockCheckDroneDto;

    @BeforeEach
    void setUp() {
        // Mock NewDroneDto
        mockDroneDto = new NewDroneDto();
        mockDroneDto.setSerialNumber("DRONE001");
        mockDroneDto.setModel("LIGHTWEIGHT");
        mockDroneDto.setWeightLimit(250);
        mockDroneDto.setBatteryCapacity(100);

        // Mock Drone (saved version)
        mockSavedDrone = new Drone();
        mockSavedDrone.setId(1L);
        mockSavedDrone.setSerialNumber("DRONE001");
        mockSavedDrone.setModel("LIGHTWEIGHT");
        mockSavedDrone.setWeightLimit(250);
        mockSavedDrone.setBatteryCapacity(100);
        mockSavedDrone.setState("IDLE");

        // Mock Medication
        mockMedication = new Medication();
        mockMedication.setName("PARACETAMOL");
        mockMedication.setWeight(100);
        mockMedication.setCode("PARA001");

        // Mock Saved Medication
        mockSavedMedication = new Medication();
        mockSavedMedication = mockMedication;
        mockSavedMedication.setId(01L);

        // Mock Medication List
        mockMedicationList = new ArrayList<>();
        mockMedicationList.add(mockMedication);

        // Mock Medication
        mockLoadMedicationDto = new LoadMedicationDto();
        mockLoadMedicationDto.setSerialNumber("DRONE001");
        mockLoadMedicationDto.setMedicationList(mockMedicationList);

        // Mock Loaded Drone
        mockLoadedDrone = new Drone();
        mockLoadedDrone.setId(1L);
        mockSavedDrone.setSerialNumber("DRONE001");
        mockSavedDrone.setModel("LIGHTWEIGHT");
        mockSavedDrone.setWeightLimit(250);
        mockSavedDrone.setBatteryCapacity(100);
        mockLoadedDrone.setState("LOADED");
        mockLoadedDrone.setMedications(mockMedicationList);

        // Mock CheckDroneDto
        mockCheckDroneDto = new CheckDroneDto();
        mockCheckDroneDto.setSerialNumber("DRONE001");

    }

    @Test
    void testRegisterDrone() {
        // mock Repository behavior
        when(droneRepository.save(any(Drone.class))).thenReturn(mockSavedDrone);

        // call service method
        Drone savedDrone = droneService.registerDrone(mockDroneDto);

        // verify interactions
        verify(droneRepository, times(1)).save(any(Drone.class));

        // check if returned drone has the expected values
        assertNotNull(savedDrone);
        assertEquals("DRONE001", savedDrone.getSerialNumber());
        assertEquals("LIGHTWEIGHT", savedDrone.getModel());
        assertEquals(250, savedDrone.getWeightLimit());
        assertEquals(100, savedDrone.getBatteryCapacity());
        assertEquals("IDLE", savedDrone.getState());
    }

    @Test
    void testLoadDroneWithMedication() {
        // mock Repository behavior
        when(droneRepository.findBySerialNumber("DRONE001")).thenReturn(mockSavedDrone);
        when(medicationRepository.findByCode("PARA001")).thenReturn(mockMedicationList.get(0));
        when(droneRepository.save(any(Drone.class))).thenReturn(mockSavedDrone);

        // call service method
        Drone savedDrone = droneService.loadDroneWithMedication(mockLoadMedicationDto);

        // verify interactions
        verify(droneRepository, times(1)).findBySerialNumber("DRONE001");
        verify(droneRepository, times(1)).save(any(Drone.class));
        verify(medicationRepository, times(1)).findByCode(anyString());
        verify(medicationRepository, times(1)).save(any(Medication.class));

        // check if returned drone has expected values
        assertNotNull(savedDrone);
        assertEquals("DRONE001", savedDrone.getSerialNumber());
        assertEquals("LOADING", savedDrone.getState());
    }

    @Test
    void testCheckLoadMedication() {
        // Mock repository behavior
        when(droneRepository.findBySerialNumber("DRONE001")).thenReturn(mockLoadedDrone);
        when(medicationRepository.findByDroneId(1L)).thenReturn(mockMedicationList);

        // Call service method
        List<Medication> medicationList = droneService.checkLoadedMedication(mockCheckDroneDto);

        // Verify interactions
        verify(droneRepository, times(1)).findBySerialNumber("DRONE001");
        verify(medicationRepository, times(1)).findByDroneId(1L);

        // Assertions
        assertNotNull(medicationList);
        assertEquals(1, medicationList.size());
        assertEquals("PARACETAMOL", medicationList.get(0).getName());
    }

    @Test
    void testCheckDroneAvailability() {
        // Mock repository behavior
        when(droneRepository.findBySerialNumber("DRONE001")).thenReturn(mockLoadedDrone);

        // Call service method
        DroneAvailabilityDto droneAvailabilityDto = droneService.checkDroneAvailability(mockCheckDroneDto);

        // Verify interactions
        verify(droneRepository, times(1)).findBySerialNumber("DRONE001");

        // Assertions
        assertNotNull(droneAvailabilityDto);
        assertEquals("DRONE001", droneAvailabilityDto.getSerialNumber());
        assertEquals(0, droneAvailabilityDto.getAvailable());
    }

    @Test
    void testCheckDroneInformation() {
        when(droneRepository.findBySerialNumber("DRONE001")).thenReturn(mockSavedDrone);

        DroneInformationDto droneInformationDto = droneService.checkDroneInformation(mockCheckDroneDto);

        verify(droneRepository, times(1)).findBySerialNumber("DRONE001");

        assertNotNull(droneInformationDto);
        assertEquals("DRONE001", droneInformationDto.getSerialNumber());
        assertEquals(100, droneInformationDto.getBattery());
    }
}
