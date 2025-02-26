package com.example.drone.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.drone.dto.CheckDroneDto;
import com.example.drone.dto.DroneAvailabilityDto;
import com.example.drone.dto.DroneInformationDto;
import com.example.drone.dto.LoadMedicationDto;
import com.example.drone.dto.NewDroneDto;
import com.example.drone.entity.Drone;
import com.example.drone.entity.Medication;
import com.example.drone.repository.DroneRepository;
import com.example.drone.repository.MedicationRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class DroneService {
    
    @Autowired
    DroneRepository droneRepository;

    @Autowired
    MedicationRepository medicationRepository;

    /**
     * Register new Drone.
     * 
     * @param newDroneDto info of new drone
     * @return saved Drone
     */
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

    /**
     * Load Medication/s into a Drone
     * 
     * @param serialNumber of Drone to be loaded
     * @param loadMedicationDto list of medications to be loaded
     * @return Drone with updated list of medications loaded
     */
    public Drone loadDroneWithMedication(LoadMedicationDto loadMedicationDto) {
        // Check for Drone availabilty
        Drone drone = droneRepository.findBySerialNumber(loadMedicationDto.getSerialNumber());

        if (drone != null) {
            if (drone.getState().equals("IDLE") && drone.getBatteryCapacity() >= 25) {
                // check if total load weight is lesser than or equal to drone weight limit
                int totalLoadWeight = checkLoadWeight(loadMedicationDto.getMedicationList());

                log.info("TOTAL LOAD WEIGHT {}", totalLoadWeight);
                
                if (totalLoadWeight <= drone.getWeightLimit()) {
                    List<Medication> loadList = new ArrayList<>();
                    for (Medication medication : loadMedicationDto.getMedicationList()) {
                        Medication checkMedication = medicationRepository.findByCode(medication.getCode());
                        if (checkMedication == null) {
                            // automatically register new Medicine if not found in db
                            checkMedication = registerMedication(medication, drone);
                        } else {
                            checkMedication.setDrone(drone);
                            medicationRepository.save(checkMedication);
                        }
                        loadList.add(checkMedication);
                    }
                    drone.setState("LOADING");
                    droneRepository.save(drone);
                    return drone;
                } else {
                    throw new RuntimeException("Load is over the weight limit of the selected Drone!");
                }
            } else if (!drone.getState().equals("IDLE")) {
                throw new RuntimeException("Unavailable! Drone is currently " + drone.getState());
            } else if (drone.getBatteryCapacity() < 25){
                throw new RuntimeException("Unavailable! Drone is @ " + drone.getBatteryCapacity() + "%");
            } else {
                throw new RuntimeException("Unavailable! Drone is @ " + drone.getBatteryCapacity() + "% and is currently " + drone.getState());
            }
        } else {
            throw new RuntimeException("Drone not found!");
        }
    }

    /**
     * Check the loaded Medication/s in a Drone
     * 
     * @param serialNumber of the Drone to be checked
     * @return list of Medication
     */
    public List<Medication> checkLoadedMedication(CheckDroneDto checkDroneDto) {
        Drone drone = droneRepository.findBySerialNumber(checkDroneDto.getSerialNumber());
        if (drone != null) {
            return medicationRepository.findByDroneId(drone.getId());
        } else {
            throw new RuntimeException("Drone not found!");
        }
    }

    /**
     * Check Drone State
     * 
     * @param serialNumber of the Drone
     * @return Drone State
     */
    public DroneAvailabilityDto checkDroneAvailability(CheckDroneDto checkDroneDto) {
        Drone drone = droneRepository.findBySerialNumber(checkDroneDto.getSerialNumber());

        DroneAvailabilityDto droneAvailabilityDto = new DroneAvailabilityDto();
        droneAvailabilityDto.setSerialNumber(checkDroneDto.getSerialNumber());

        if(drone != null) {
            if (drone.getBatteryCapacity() >= 25 && drone.getState().equals("IDLE")){
                droneAvailabilityDto.setAvailabilty(true);
            } else {
                droneAvailabilityDto.setAvailabilty(false);
            }
            return droneAvailabilityDto;
        } else {
            throw new RuntimeException("Drone not found!");
        }
    }

    /**
     * Check Drone Battery Percentage
     * 
     * @param serialNumber of the Drone
     * @return battery percentage of the Drone
     */
    public DroneInformationDto checkDroneInformation(CheckDroneDto checkDroneDto) {
        Drone drone = droneRepository.findBySerialNumber(checkDroneDto.getSerialNumber());

        DroneInformationDto droneInformationDto = new DroneInformationDto();
        droneInformationDto.setSerialNumber(checkDroneDto.getSerialNumber());

        if(drone != null) {
            droneInformationDto.setBattery(drone.getBatteryCapacity());
            return droneInformationDto;
        } else {
            throw new RuntimeException("Drone not found!");
        }
    }

    /**
     * Check if total load of medication is <= drone weight Capacity
     * 
     * @param loadMedicationList to be loaded
     * @return total weight of load
     */
    private int checkLoadWeight(List<Medication> loadMedicationList) {
        int totalWeight = 0;
        for (Medication medication : loadMedicationList) {
            totalWeight += medication.getWeight();
        }
        return totalWeight;
    }

    /**
     * Register a Medication in the db if new
     * 
     * @param medication details
     * @param drone details
     * @return newly created Medication
     */
    private Medication registerMedication(Medication medication, Drone drone) {
        boolean isNameValid = medicationNameCheck(medication.getName());
        boolean isCodeValid = medicationCodeCheck(medication.getCode());

        if (isNameValid && isCodeValid) {
            medication.setDrone(drone);
            Medication newMedication = medicationRepository.save(medication);
            return newMedication;
        } else {
            throw new RuntimeException("Medication name and/or code is invalid!");
        }   
    }

    /**
     * Check if Medication name only contains letters, numbers, -, and _
     * 
     * @param name of Mediation
     * @return boolean
     */
    private boolean medicationNameCheck(String name) {
        if (name.matches("[a-zA-Z0-9_-]+")) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Check if Medication code only contains uppercase letters, underscores, and numbers
     * 
     * @param code
     * @return
     */
    private boolean medicationCodeCheck(String code) {
        if (code.matches("[A-Z0-9_]+")) {
            return true;
        } else {
            return false;
        }
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
