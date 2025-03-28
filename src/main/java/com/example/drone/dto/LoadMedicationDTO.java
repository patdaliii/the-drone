package com.example.drone.dto;

import java.util.List;

import com.example.drone.entity.Medication;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoadMedicationDto {
    private String serialNumber;
    private List<Medication> medicationList;
}
