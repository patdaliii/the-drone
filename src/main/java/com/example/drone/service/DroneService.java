package com.example.drone.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.drone.dto.LoadMedicationDto;
import com.example.drone.dto.NewDroneDto;
import com.example.drone.entity.Drone;
import com.example.drone.entity.Medication;
import com.example.drone.repository.DroneRepository;
import com.example.drone.repository.MedicationRepository;

@Service
public class DroneService {
    
    @Autowired
    DroneRepository droneRepository;

    @Autowired
    MedicationRepository medicationRepository;

    public Drone registerDrone(NewDroneDto newDroneDto) {
        try {
            Drone newDrone = new Drone();
            newDrone.setSerialNumber(newDroneDto.getSerialNumber());
            newDrone.setModel(newDroneDto.getModel());
            newDrone.setWeightLimit(newDroneDto.getWeightLimit());
            newDrone.setBatteryCapacity(newDroneDto.getBatteryCapacity());
            newDrone.setState("IDLE");
            droneRepository.save(newDrone);
            return newDrone;
        } catch (Exception e) {
            throw new RuntimeException("Failed to register drone", e);
        }
    }

    public Drone loadDroneWithMedication(String serialNumber, LoadMedicationDto loadMedicationDto) {
        // Check for Drone availabilty
        Drone drone = droneRepository.findBySerialNumber(serialNumber);

        if (drone != null) {
            if (drone.getState().equals("IDLE") && drone.getBatteryCapacity() >= 25) {
                for (Medication medication : loadMedicationDto.getMedicationList()) {
                    Medication checkMedication = medicationRepository.findByCode(medication.getCode());
                    if (checkMedication == null) {
                        // automatically register new Medicine if not found in db
                        registerMedication(medication, drone);
                    }
                }
                drone.setState("LOADING");
                return drone;
            } else {
                throw new RuntimeException("Unavailable! Drone is " + drone.getState() + " & @ " + drone.getBatteryCapacity() + "%");
            }
        } else {
            throw new RuntimeException("Drone not found!");
        }
    }

    public List<Medication> checkLoadedMedication(String serialNumber) {
        Drone drone = droneRepository.findBySerialNumber(serialNumber);

        if (drone != null) {
            return medicationRepository.findByDroneId(drone.getId());
        } else {
            throw new RuntimeException("Drone not found!");
        }
    }

    public String checkDroneAvailability(Long id) {
        Optional<Drone> drone = droneRepository.findById(id);

        if(drone.isPresent()) {
            return drone.get().getState();
        } else {
            throw new RuntimeException("Drone not found!");
        }
    }

    public String checkDroneInformation(Long id) {
        Optional<Drone> drone = droneRepository.findById(id);

        if(drone.isPresent()) {
            return new String ("Drone " + String.valueOf(drone.get().getSerialNumber()) + " is currently at " + String.valueOf(drone.get().getBatteryCapacity()) + "%");
        } else {
            throw new RuntimeException("Drone not found!");
        }
    }

    private Medication registerMedication(Medication medication, Drone drone) {
        medication.setDrone(drone);
        Medication newMedication = medicationRepository.save(medication);
        return newMedication;
    }

    public List<Drone> getDronesList() {
        return droneRepository.findAll();
    }

    public List<Medication> getMedicationsList() {
        return medicationRepository.findAll();
    }

    public Drone getDroneBySerialNumber(String serialNumber) {
        return droneRepository.findBySerialNumber(serialNumber);
    }
}
