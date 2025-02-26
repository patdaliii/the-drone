package com.example.drone.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.drone.dto.LoadMedicationDto;
import com.example.drone.dto.NewDroneDto;
import com.example.drone.entity.Drone;
import com.example.drone.entity.Medication;
import com.example.drone.repository.MedicationRepository;
import com.example.drone.service.DroneService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/api/v1")
public class DroneController {

    @Autowired
    DroneService droneService;

    @Autowired
    MedicationRepository medicationRepository;
    
    /**
     * Registers a Drone
     * 
     * @param drone new drone
     * @return new drone
     */
    @PostMapping("/registerDrone")
    public ResponseEntity<Drone> registerDrone(@RequestBody NewDroneDto newDrone) {
        return new ResponseEntity<>(droneService.registerDrone(newDrone), HttpStatus.CREATED);
    }
    
    /**
     * Load a List of Medication into a Drone via Serial Number
     * 
     * @param serialNumber of drone
     * @param loadMedication list of medication to be loaded
     * @return drone with updated list of loaded medication
     */
    @PutMapping("/loadDrone/{serialNumber}")
    public ResponseEntity<Drone> loadDrone(@PathVariable(value = "serialNumber") String serialNumber, @RequestBody LoadMedicationDto loadMedicationDto) {
        System.out.println("loadMedication" + loadMedicationDto);
        return new ResponseEntity<>(droneService.loadDroneWithMedication(serialNumber, loadMedicationDto), HttpStatus.OK);
    }
    
    /**
     * Check loaded medications for a given drone
     * 
     * @param id of the drone
     * @return medication
     */
    @GetMapping("/checkLoadedMedication/{serialNumber}")
    public ResponseEntity<List<Medication>> getLoadedMedication(@PathVariable(value = "serialNumber") String serialNumber) {
        return new ResponseEntity<>(droneService.checkLoadedMedication(serialNumber), HttpStatus.OK);
    }

    /**
     * Check Drone State
     * 
     * @param id of the drone
     * @return drone state
     */
    @GetMapping("/checkDroneAvailability/{serialNumber}")
    public ResponseEntity<String> checkDroneAvailability(@PathVariable(value = "serialNumber") String serialNumber) {
        return new ResponseEntity<>(droneService.checkDroneAvailability(serialNumber), HttpStatus.OK);
    }
    
    /**
     * Check Drone Battery Percentage
     * 
     * @param serialNumber of the Drone
     * @return battery percentage of the drone
     */
    @GetMapping("/checkDroneInformation/{serialNumber}")
    public ResponseEntity<String> checkDroneInformation(@PathVariable(value = "serialNumber") String serialNumber) {
        return new ResponseEntity<>(droneService.checkDroneInformation(serialNumber), HttpStatus.OK);
    }

    @GetMapping("/getDrones")
    public ResponseEntity<List<Drone>> getDronesList() {
        return new ResponseEntity<>(droneService.getDronesList(), HttpStatus.OK);
    }
    
    @GetMapping("/getMedications")
    public ResponseEntity<List<Medication>> getMedicationsList() {
        return new ResponseEntity<>(droneService.getMedicationsList(), HttpStatus.OK);
    }

    @GetMapping("/getMedication/{code}")
    public ResponseEntity<Medication> getMedication(@PathVariable(value = "code") String code) {
        return new ResponseEntity<>(medicationRepository.findByCode(code), HttpStatus.OK);
    }
    
    @GetMapping("/{serialNumber}")
    public Drone getDroneWithMedications(@PathVariable String serialNumber) {
        return droneService.getDroneBySerialNumber(serialNumber);
    }
}
