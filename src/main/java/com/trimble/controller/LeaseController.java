package com.trimble.controller;

import com.trimble.dto.ApiResponse;
import com.trimble.dto.LeaseDto;
import com.trimble.service.LeaseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

/**
 * REST Controller for Lease operations
 */
@RestController
@RequestMapping("/api/leases")
@RequiredArgsConstructor
@Slf4j
public class LeaseController {
    
    private final LeaseService leaseService;
    
    /**
     * Start a new lease
     */
    @PostMapping("/start")
    public ResponseEntity<ApiResponse<LeaseDto>> startLease(@Valid @RequestBody LeaseDto leaseDto) {
        log.info("Lease start request received for car ID: {}", leaseDto.getCarId());
        
        LeaseDto startedLease = leaseService.startLease(leaseDto);
        
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Lease started successfully", startedLease));
    }
    
    /**
     * End a lease
     */
    @PutMapping("/{leaseId}/end")
    public ResponseEntity<ApiResponse<LeaseDto>> endLease(@PathVariable Long leaseId) {
        log.info("Lease end request received for lease ID: {}", leaseId);
        
        LeaseDto endedLease = leaseService.endLease(leaseId);
        
        return ResponseEntity.ok(ApiResponse.success("Lease ended successfully", endedLease));
    }
    
    /**
     * Get lease history by customer
     */
    @GetMapping("/customer/{customerId}/history")
    public ResponseEntity<ApiResponse<List<LeaseDto>>> getLeaseHistoryByCustomer(@PathVariable Long customerId) {
        log.info("Fetching lease history for customer: {}", customerId);
        
        List<LeaseDto> leases = leaseService.getLeaseHistoryByCustomer(customerId);
        
        return ResponseEntity.ok(ApiResponse.success("Lease history fetched successfully", leases));
    }
    
    /**
     * Get active leases by customer
     */
    @GetMapping("/customer/{customerId}/active")
    public ResponseEntity<ApiResponse<List<LeaseDto>>> getActiveLeasesByCustomer(@PathVariable Long customerId) {
        log.info("Fetching active leases for customer: {}", customerId);
        
        List<LeaseDto> leases = leaseService.getActiveLeasesByCustomer(customerId);
        
        return ResponseEntity.ok(ApiResponse.success("Active leases fetched successfully", leases));
    }
    
    /**
     * Get lease history by car
     */
    @GetMapping("/car/{carId}/history")
    public ResponseEntity<ApiResponse<List<LeaseDto>>> getLeaseHistoryByCar(@PathVariable Long carId) {
        log.info("Fetching lease history for car: {}", carId);
        
        List<LeaseDto> leases = leaseService.getLeaseHistoryByCar(carId);
        
        return ResponseEntity.ok(ApiResponse.success("Lease history fetched successfully", leases));
    }
}