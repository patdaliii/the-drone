package com.example.drone.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.ArrayList;
import java.util.List;

import com.example.drone.dto.CheckDroneDto;
import com.example.drone.dto.DroneAvailabilityDto;
import com.example.drone.dto.DroneInformationDto;
import com.example.drone.dto.LoadMedicationDto;
import com.example.drone.dto.NewDroneDto;
import com.example.drone.entity.Drone;
import com.example.drone.entity.Medication;
import com.example.drone.service.DroneService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(MockitoExtension.class)
public class DroneControllerTest {
    
    private MockMvc mockMvc;

    @Mock
    private DroneService droneService;

    @InjectMocks
    private DroneController droneController;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private Drone mockDrone;
    private NewDroneDto mockNewDroneDto;
    private Drone mockLoadedDrone;
    private LoadMedicationDto mockLoadMedicationDto;
    private Medication mockMedication;
    private List<Medication> mockMedicationList;
    private DroneAvailabilityDto mockDroneAvailabilityDto;
    private CheckDroneDto mockCheckDroneDto;
    private DroneInformationDto mockDroneInformationDto;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(droneController).build();
        
        // Mock Drone
        mockDrone = new Drone();
        mockDrone.setId(1L);
        mockDrone.setSerialNumber("DRONE001");
        mockDrone.setModel("LIGHTWEIGHT");
        mockDrone.setWeightLimit(250);
        mockDrone.setBatteryCapacity(100);
        mockDrone.setState("IDLE");

        // Mock NewDroneDto
        mockNewDroneDto = new NewDroneDto();
        mockNewDroneDto.setSerialNumber("DRONE001");
        mockNewDroneDto.setModel("LIGHTWEIGHT");
        mockNewDroneDto.setWeightLimit(250);
        mockNewDroneDto.setBatteryCapacity(100);

        // Mock Medication
        mockMedication = new Medication();
        mockMedication.setName("PARACETAMOL");
        mockMedication.setWeight(100);
        mockMedication.setCode("PARA001");
        mockMedication.setImage(null);

        // Mock LoadMedicationDto
        mockLoadMedicationDto = new LoadMedicationDto();
        mockLoadMedicationDto.setSerialNumber("DRONE001");
        mockMedicationList = new ArrayList<>();
        mockMedicationList.add(mockMedication);

        // Mock Drone with loaded Medication
        mockLoadedDrone = new Drone();
        mockLoadedDrone = mockDrone;
        mockLoadedDrone.setState("LOADED");
        mockLoadedDrone.setMedications(mockMedicationList);

        // Mock DroneAvailabilityDto
        mockDroneAvailabilityDto = new DroneAvailabilityDto();
        mockDroneAvailabilityDto.setAvailable(1);
        mockDroneAvailabilityDto.setSerialNumber("DRONE001");

        // Mock CheckDroneDto
        mockCheckDroneDto = new CheckDroneDto();
        mockCheckDroneDto.setSerialNumber("DRONE001");

        // Mock DroneInformationDto
        mockDroneInformationDto = new DroneInformationDto();
        mockDroneInformationDto.setSerialNumber("DRONE001");
        mockDroneInformationDto.setBattery(100);
    }

    @Test
    void testRegisterNewDrone() throws Exception {
        when(droneService.registerDrone(any(NewDroneDto.class))).thenReturn(mockDrone);

        mockMvc.perform(post("/api/v1/drone/registerDrone")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(mockNewDroneDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.serialNumber").value("DRONE001"))
                .andExpect(jsonPath("$.model").value("LIGHTWEIGHT"))
                .andExpect(jsonPath("$.weightLimit").value(250))
                .andExpect(jsonPath("$.batteryCapacity").value(100))
                .andExpect(jsonPath("$.state").value("IDLE"));

        verify(droneService, times(1)).registerDrone(any(NewDroneDto.class));
    }

    @Test
    void testLoadDrone() throws Exception {
        when(droneService.loadDroneWithMedication(any(LoadMedicationDto.class))).thenReturn(mockLoadedDrone);

        mockMvc.perform(put("/api/v1/drone/loadDrone")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(mockNewDroneDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.serialNumber").value("DRONE001"))
                .andExpect(jsonPath("$.model").value("LIGHTWEIGHT"))
                .andExpect(jsonPath("$.weightLimit").value(250))
                .andExpect(jsonPath("$.batteryCapacity").value(100))
                .andExpect(jsonPath("$.state").value("LOADED"));

        verify(droneService, times(1)).loadDroneWithMedication(any(LoadMedicationDto.class));
    }

    @Test
    void testCheckDroneAvailability() throws Exception {
        when(droneService.checkDroneAvailability(any(CheckDroneDto.class))).thenReturn(mockDroneAvailabilityDto);

        mockMvc.perform(get("/api/v1/drone/checkDroneAvailability")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(mockCheckDroneDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.serialNumber").value("DRONE001"))
                .andExpect(jsonPath("$.available").value(1));

        verify(droneService, times(1)).checkDroneAvailability(any(CheckDroneDto.class));
    }

    @Test
    void testCheckDroneInformation() throws Exception {
        when(droneService.checkDroneInformation(any(CheckDroneDto.class))).thenReturn(mockDroneInformationDto);

        mockMvc.perform(get("/api/v1/drone/checkDroneInformation")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(mockCheckDroneDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.serialNumber").value("DRONE001"))
                .andExpect(jsonPath("$.battery").value(100));

        verify(droneService, times(1)).checkDroneInformation(any(CheckDroneDto.class));
    }

}
