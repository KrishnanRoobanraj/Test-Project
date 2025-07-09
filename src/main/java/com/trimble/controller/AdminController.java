package com.trimble.controller;

import com.trimble.dto.ApiResponse;
import com.trimble.dto.CarDto;
import com.trimble.dto.CustomerDto;
import com.trimble.dto.LeaseDto;
import com.trimble.enums.CarStatus;
import com.trimble.service.AdminService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for Admin operations
 * Admin can perform all operations on cars, customers, and leases
 */
@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@Slf4j
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {
    
    private final AdminService adminService;
    
    /**
     * Get all cars in the system
     */
    @GetMapping("/cars")
    public ResponseEntity<ApiResponse<List<CarDto>>> getAllCars() {
        log.info("Admin requesting all cars");
        
        List<CarDto> cars = adminService.getAllCars();
        
        return ResponseEntity.ok(ApiResponse.success("All cars fetched successfully", cars));
    }
    
    /**
     * Get all customers in the system
     */
    @GetMapping("/customers")
    public ResponseEntity<ApiResponse<List<CustomerDto>>> getAllCustomers() {
        log.info("Admin requesting all customers");
        
        List<CustomerDto> customers = adminService.getAllCustomers();
        
        return ResponseEntity.ok(ApiResponse.success("All customers fetched successfully", customers));
    }
    
    /**
     * Get all leases in the system
     */
    @GetMapping("/leases")
    public ResponseEntity<ApiResponse<List<LeaseDto>>> getAllLeases() {
        log.info("Admin requesting all leases");
        
        List<LeaseDto> leases = adminService.getAllLeases();
        
        return ResponseEntity.ok(ApiResponse.success("All leases fetched successfully", leases));
    }
    
    /**
     * Update car status
     */
    @PutMapping("/cars/{carId}/status")
    public ResponseEntity<ApiResponse<CarDto>> updateCarStatus(
            @PathVariable Long carId,
            @RequestParam CarStatus status) {
        
        log.info("Admin updating car status for ID: {} to {}", carId, status);
        
        CarDto updatedCar = adminService.updateCarStatus(carId, status);
        
        return ResponseEntity.ok(ApiResponse.success("Car status updated successfully", updatedCar));
    }
    
    /**
     * End any lease
     */
    @PutMapping("/leases/{leaseId}/end")
    public ResponseEntity<ApiResponse<LeaseDto>> endLease(@PathVariable Long leaseId) {
        log.info("Admin ending lease with ID: {}", leaseId);
        
        LeaseDto endedLease = adminService.endLease(leaseId);
        
        return ResponseEntity.ok(ApiResponse.success("Lease ended successfully", endedLease));
    }
    
    
} 
