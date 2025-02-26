package com.example.drone.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.drone.entity.Drone;

public interface DroneRepository extends JpaRepository<Drone, Long> {
    Drone findBySerialNumber(String serialNumber);

    List<Drone> findByState(String state);
}
