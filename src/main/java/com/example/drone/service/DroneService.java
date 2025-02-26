package com.example.drone.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.drone.dto.LoadMedicationDTO;
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

    public Drone registerDrone(Drone drone) {
        try {
            Drone newDrone = droneRepository.save(drone);
            return newDrone;
        } catch (Exception e) {
            throw new RuntimeException("Failed to register drone", e);
        }
    }

    public Optional<Drone> loadDroneWithMedication(String serialNumber, LoadMedicationDTO loadMedication) {
        // Check for Drone availabilty
        // TODO: add loading of medication to DRONE
        Optional<Drone> drone = droneRepository.findBySerialNumber(serialNumber);

        if (drone.isPresent()) {
            String droneAvailability = drone.get().getState();
            if (droneAvailability.equals("IDLE")) {
                for (Medication medication : loadMedication.getMedicationList()) {
                    Medication checkMedication = medicationRepository.findByCode(medication.getCode());
                    if (checkMedication == null) {

                    }
                }
                drone.get().setState("LOADED");
                return drone;
            } else {
                throw new RuntimeException("Unavailable! Drone is " + droneAvailability);
            }
        } else {
            throw new RuntimeException("Drone not found!");
        }
    }

    public List<Medication> checkLoadedMedication(String serialNumber) {
        Optional<Drone> drone = droneRepository.findBySerialNumber(serialNumber);

        if (drone.isPresent()) {
            return medicationRepository.findByDroneId(drone.get().getId());
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

    private Medication registerMedication(Medication medication, Long droneId) {
        medication.setDrone(null);
        return medicationRepository.findByCode(code);
    }

    public List<Drone> getDronesList() {
        return droneRepository.findAll();
    }

    public List<Medication> getMedicationsList() {
        return medicationRepository.findAll();
    }

    public Optional<Drone> getDroneBySerialNumber(String serialNumber) {
        return droneRepository.findBySerialNumber(serialNumber);
    }
}
