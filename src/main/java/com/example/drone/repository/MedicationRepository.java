package com.example.drone.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.drone.entity.Medication;

public interface MedicationRepository extends JpaRepository<Medication, Long>{
    List<Medication> findByDroneId(Long droneId);

    Medication findByCode(String code);
}
