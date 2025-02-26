package com.example.drone.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DroneAvailabilityDto {
    private String serialNumber;
    private boolean availabilty;
}
