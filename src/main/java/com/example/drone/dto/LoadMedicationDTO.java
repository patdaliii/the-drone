package com.example.drone.dto;

import java.util.List;

import com.example.drone.entity.Medication;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoadMedicationDto {
    private List<Medication> medicationList;
}
