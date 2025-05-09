package com.example.drone.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.drone.entity.Drone;
import com.example.drone.entity.Medication;
import com.example.drone.repository.DroneRepository;
import com.example.drone.repository.MedicationRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@Transactional
public class DroneScheduler {
    
    @Autowired
    DroneRepository droneRepository;

    @Autowired
    MedicationRepository medicationRepository;

    private final String idleState = "IDLE";
    private final String loadingState = "LOADING";
    private final String loadedState = "LOADED";
    private final String deliveringState = "DELIVERING";
    private final String deliveredState = "DELIVERED";
    private final String returningState = "RETURNING";

    @Scheduled(fixedRate = 10000)
    public void updateLoadingToLoaded() {
        List<Drone> loadingDrones = droneRepository.findByState(loadingState);

        for (Drone drone : loadingDrones) {
            drone.setState(loadedState);
        }

        droneRepository.saveAll(loadingDrones);
        log.info("All LOADING drones are updated to LOADED");
    }

    @Scheduled(fixedRate = 5000)
    public void updateLoadedToDelivering() {
        List<Drone> loadedDrones = droneRepository.findByState(loadedState);

        for (Drone drone : loadedDrones) {
            drone.setState(deliveringState);
        }

        droneRepository.saveAll(loadedDrones);
        log.info("All LOADED drones are updated to DELIVERING");
    }

    @Scheduled(fixedRate = 20000) 
    public void updateDeliveringToDelivered() {
        List<Drone> deliveringDrones = droneRepository.findByState(deliveringState);

        for (Drone drone : deliveringDrones) {
            drone.setState(deliveredState);
            drone.setBatteryCapacity(drone.getBatteryCapacity() - 40);
            log.info("drone " + drone.toString());

            // remove Drone column in Medication Table
            updateMedication(drone.getMedications());
            drone.setMedications(null);
        }

        droneRepository.saveAll(deliveringDrones);
        log.info("All DELIVERING drones are updated to DELIVERED");
    }

    @Scheduled(fixedRate = 5000)
    public void updateDeliveredToReturning() {
        List<Drone> deliveredDrones = droneRepository.findByState(deliveredState);

        for (Drone drone : deliveredDrones) {
            drone.setState(returningState);
        }

        droneRepository.saveAll(deliveredDrones);
        log.info("All DELIVERED drones are updated to RETURNING");
    }

    @Scheduled(fixedRate = 10000)
    public void updateReturningToIdle() {
        List<Drone> returningDrones = droneRepository.findByState(returningState);

        for(Drone drone : returningDrones) {
            drone.setState(idleState);
            drone.setBatteryCapacity(drone.getBatteryCapacity() - 20);
        }

        droneRepository.saveAll(returningDrones);
        log.info("All RETURNING drones are updated to IDLE");
    }

    @Scheduled(fixedRate = 10000)
    public void fullyChargeIdleDrones() {
        List<Drone> idleDrones = droneRepository.findByState(idleState);

        for (Drone drone : idleDrones) {
            drone.setBatteryCapacity(100);
        }

        droneRepository.saveAll(idleDrones);
        log.info("All IDLE drones have been fully charged to 100%");
    }

    private void updateMedication(List<Medication> loadList) {
        for (Medication medication : loadList) {
            medication.setDrone(null);
        }
        medicationRepository.saveAll(loadList);
    }
}
