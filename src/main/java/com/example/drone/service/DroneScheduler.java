package com.example.drone.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.example.drone.entity.Drone;
import com.example.drone.repository.DroneRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class DroneScheduler {
    
    @Autowired
    DroneRepository droneRepository;

    private final String idleState = "IDLE";
    private final String loadingState = "LOADING";
    private final String loadedState = "LOADED";
    private final String deliveringState = "DELIVERING";
    private final String deliveredState = "DELIVERED";
    private final String returningState = "RETURNING";

    @Scheduled(initialDelay = 10000, fixedRate = 10000)
    public void updateLoadingToLoaded() {
        List<Drone> loadingDrones = droneRepository.findByState(loadingState);

        for (Drone drone : loadingDrones) {
            drone.setState(loadedState);
            drone.setBatteryCapacity(drone.getBatteryCapacity() - 5);
        }

        droneRepository.saveAll(loadingDrones);
        log.info("All LOADING drones are updated to LOADED");
    }

    @Scheduled(initialDelay = 15000, fixedRate = 5000)
    public void updateLoadedToDelivering() {
        List<Drone> loadedDrones = droneRepository.findByState(loadedState);

        for (Drone drone : loadedDrones) {
            drone.setState(deliveringState);
        }

        droneRepository.saveAll(loadedDrones);
        log.info("All LOADED drones are updated to DELIVERING");
    }

    @Scheduled(initialDelay = 35000, fixedRate = 20000) 
    public void updateDeliveringToDelivered() {
        List<Drone> deliveringDrones = droneRepository.findByState(deliveringState);

        for (Drone drone : deliveringDrones) {
            drone.setState(deliveredState);
            drone.setBatteryCapacity(drone.getBatteryCapacity() - 20);
        }

        droneRepository.saveAll(deliveringDrones);
        log.info("All DELIVERING drones are updated to DELIVERED");
    }

    @Scheduled(initialDelay = 45000, fixedRate = 5000)
    public void updateDeliveredToReturning() {
        List<Drone> deliveredDrones = droneRepository.findByState(deliveredState);

        for (Drone drone : deliveredDrones) {
            drone.setState(returningState);
        }

        droneRepository.saveAll(deliveredDrones);
        log.info("All DELIVERED drones are updated to RETURNING");
    }

    @Scheduled(initialDelay = 55000, fixedRate = 10000)
    public void updateReturningToIdle() {
        List<Drone> returningDrones = droneRepository.findByState(returningState);

        for(Drone drone : returningDrones) {
            drone.setState(idleState);
            drone.setBatteryCapacity(drone.getBatteryCapacity() - 10);
        }

        droneRepository.saveAll(returningDrones);
        log.info("All RETURNING drones are updated to IDLE");
    }

    @Scheduled(initialDelay = 65000, fixedRate = 10000)
    public void fullyChargeIdleDrones() {
        List<Drone> idleDrones = droneRepository.findByState(idleState);

        for (Drone drone : idleDrones) {
            drone.setBatteryCapacity(100);
        }

        droneRepository.saveAll(idleDrones);
        log.info("All IDLE drones have been fully charged to 100%");
    }
}
