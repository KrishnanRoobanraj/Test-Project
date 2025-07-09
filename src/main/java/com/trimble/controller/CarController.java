package com.trimble.controller;

import com.trimble.dto.ApiResponse;
import com.trimble.dto.CarDto;
import com.trimble.service.CarService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

/**
 * REST Controller for Car operations
 * Handles car owner related operations
 */
@RestController
@RequestMapping("/api/cars")
@RequiredArgsConstructor
@Slf4j
public class CarController {
    
    private final CarService carService;
    
    /**
     * Register a new car
     */
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<CarDto>> registerCar(
            @Valid @RequestBody CarDto carDto,
            Authentication authentication) {
        
        log.info("Car registration request received for license plate: {}", carDto.getLicensePlate());
        
        // For simplicity, using a hardcoded owner ID
        // In real implementation, get owner ID from authentication
        Long ownerId = 1L; // This should be extracted from authentication
        
        CarDto registeredCar = carService.registerCar(carDto, ownerId);
        
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Car registered successfully", registeredCar));
    }
    
    /**
     * Get all cars by owner
     */
    @GetMapping("/owner/{ownerId}")
    public ResponseEntity<ApiResponse<List<CarDto>>> getCarsByOwner(@PathVariable Long ownerId) {
        log.info("Fetching cars for owner: {}", ownerId);
        
        List<CarDto> cars = carService.getCarsByOwner(ownerId);
        
        return ResponseEntity.ok(ApiResponse.success("Cars fetched successfully", cars));
    }
    
    /**
     * Get all available cars
     */
    @GetMapping("/available")
    public ResponseEntity<ApiResponse<List<CarDto>>> getAvailableCars() {
        log.info("Fetching available cars");
        
        List<CarDto> cars = carService.getAvailableCars();
        
        return ResponseEntity.ok(ApiResponse.success("Available cars fetched successfully", cars));
    }
    
    /**
     * Get car by ID
     */
    @GetMapping("/{carId}")
    public ResponseEntity<ApiResponse<CarDto>> getCarById(@PathVariable Long carId) {
        log.info("Fetching car with ID: {}", carId);
        
        CarDto car = carService.getCarById(carId);
        
        return ResponseEntity.ok(ApiResponse.success("Car fetched successfully", car));
    }
}