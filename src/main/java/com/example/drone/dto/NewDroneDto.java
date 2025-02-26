package com.example.drone.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NewDroneDto {
    private String serialNumber;
    private String model;
    private int weightLimit;
    private int batteryCapacity;
}
