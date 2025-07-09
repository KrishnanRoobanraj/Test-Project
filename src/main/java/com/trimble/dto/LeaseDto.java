package com.trimble.dto;

import com.trimble.enums.LeaseStatus;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Data Transfer Object for Lease entity
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LeaseDto {
    
    private Long id;
    
    @NotNull(message = "Car ID is required")
    private Long carId;
    
    private Long customerId;
    
    private String carDetails;
    
    private String customerName;
    
    @NotNull(message = "Start date is required")
    private LocalDateTime startDate;
    
    private LocalDateTime endDate;
    
    private BigDecimal totalAmount;
    
    private LeaseStatus status;
    
    private LocalDateTime createdAt;
    
    private LocalDateTime updatedAt;
}
