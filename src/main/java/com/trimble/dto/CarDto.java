package com.trimble.dto;

import com.trimble.enums.CarStatus;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Min;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Data Transfer Object for Car entity
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CarDto {
    
    private Long id;
    
    @NotBlank(message = "License plate is required")
    private String licensePlate;
    
    @NotBlank(message = "Make is required")
    private String make;
    
    @NotBlank(message = "Model is required")
    private String model;
    
    @NotNull(message = "Year is required")
    @Min(value = 1900, message = "Year must be greater than 1900")
    private Integer year;
    
    @NotBlank(message = "Color is required")
    private String color;
    
    @NotNull(message = "Daily rate is required")
    @Positive(message = "Daily rate must be positive")
    private BigDecimal dailyRate;
    
    private CarStatus status;
    
    private Long ownerId;
    
    private String ownerName;
    
    private LocalDateTime createdAt;
    
    private LocalDateTime updatedAt;
}
