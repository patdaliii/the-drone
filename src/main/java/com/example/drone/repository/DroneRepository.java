package com.example.drone.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.drone.entity.Drone;

public interface DroneRepository extends JpaRepository<Drone, Long> {
    Optional<Drone> findBySerialNumber(String serialNumber);
}
