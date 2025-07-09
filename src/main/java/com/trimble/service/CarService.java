package com.trimble.service;

import com.trimble.dto.CarDto;
import com.trimble.entity.Car;
import com.trimble.entity.User;
import com.trimble.enums.CarStatus;
import com.trimble.exception.CarNotFoundException;
import com.trimble.repository.CarRepository;
import com.trimble.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service class for Car operations
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class CarService {
    
    private final CarRepository carRepository;
    private final UserRepository userRepository;
    
    /**
     * Register a new car
     */
    public CarDto registerCar(CarDto carDto, Long ownerId) {
        log.info("Registering new car with license plate: {} for owner: {}", 
                carDto.getLicensePlate(), ownerId);
        
        // Check if car already exists
        if (carRepository.existsByLicensePlate(carDto.getLicensePlate())) {
            throw new RuntimeException("Car with license plate " + carDto.getLicensePlate() + " already exists");
        }
        
        // Get owner
        User owner = userRepository.findById(ownerId)
                .orElseThrow(() -> new RuntimeException("Owner not found"));
        
        // Create car entity
        Car car = new Car();
        car.setLicensePlate(carDto.getLicensePlate());
        car.setMake(carDto.getMake());
        car.setModel(carDto.getModel());
        car.setYear(carDto.getYear());
        car.setColor(carDto.getColor());
        car.setDailyRate(carDto.getDailyRate());
        car.setStatus(CarStatus.AVAILABLE);
        car.setOwner(owner);
        
        Car savedCar = carRepository.save(car);
        log.info("Car registered successfully with ID: {}", savedCar.getId());
        
        return convertToDto(savedCar);
    }
    
    /**
     * Get all cars by owner
     */
    @Transactional(readOnly = true)
    public List<CarDto> getCarsByOwner(Long ownerId) {
        log.info("Fetching cars for owner: {}", ownerId);
        
        List<Car> cars = carRepository.findByOwnerId(ownerId);
        return cars.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    /**
     * Get all available cars
     */
    @Transactional(readOnly = true)
    public List<CarDto> getAvailableCars() {
        log.info("Fetching all available cars");
        
        List<Car> cars = carRepository.findAvailableCars();
        return cars.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    /**
     * Get car by ID
     */
    @Transactional(readOnly = true)
    public CarDto getCarById(Long carId) {
        log.info("Fetching car with ID: {}", carId);
        
        Car car = carRepository.findById(carId)
                .orElseThrow(() -> new CarNotFoundException("Car not found with ID: " + carId));
        
        return convertToDto(car);
    }
    
    /**
     * Update car status
     */
    public CarDto updateCarStatus(Long carId, CarStatus status) {
        log.info("Updating car status for ID: {} to {}", carId, status);
        
        Car car = carRepository.findById(carId)
                .orElseThrow(() -> new CarNotFoundException("Car not found with ID: " + carId));
        
        car.setStatus(status);
        Car updatedCar = carRepository.save(car);
        
        log.info("Car status updated successfully");
        return convertToDto(updatedCar);
    }
    
    /**
     * Get all cars
     */
    @Transactional(readOnly = true)
    public List<CarDto> getAllCars() {
        log.info("Fetching all cars");
        
        List<Car> cars = carRepository.findAll();
        return cars.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    /**
     * Convert Car entity to CarDto
     */
    private CarDto convertToDto(Car car) {
        CarDto dto = new CarDto();
        dto.setId(car.getId());
        dto.setLicensePlate(car.getLicensePlate());
        dto.setMake(car.getMake());
        dto.setModel(car.getModel());
        dto.setYear(car.getYear());
        dto.setColor(car.getColor());
        dto.setDailyRate(car.getDailyRate());
        dto.setStatus(car.getStatus());
        dto.setOwnerId(car.getOwner().getId());
        dto.setOwnerName(car.getOwner().getFullName());
        dto.setCreatedAt(car.getCreatedAt());
        dto.setUpdatedAt(car.getUpdatedAt());
        return dto;
    }
}