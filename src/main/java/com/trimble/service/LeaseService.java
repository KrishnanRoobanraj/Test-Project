package com.trimble.service;

import com.trimble.dto.LeaseDto;
import com.trimble.entity.Car;
import com.trimble.entity.Customer;
import com.trimble.entity.Lease;
import com.trimble.enums.CarStatus;
import com.trimble.enums.LeaseStatus;
import com.trimble.exception.CarNotFoundException;
import com.trimble.exception.CustomerNotFoundException;
import com.trimble.exception.LeaseException;
import com.trimble.repository.CarRepository;
import com.trimble.repository.CustomerRepository;
import com.trimble.repository.LeaseRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service class for Lease operations
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class LeaseService {
    
    private final LeaseRepository leaseRepository;
    private final CarRepository carRepository;
    private final CustomerRepository customerRepository;
    
    /**
     * Start a new lease
     */
    public LeaseDto startLease(LeaseDto leaseDto) {
        log.info("Starting new lease for car ID: {} and customer ID: {}", 
                leaseDto.getCarId(), leaseDto.getCustomerId());
        
        // Validate customer lease limit (max 2 active leases)
        long activeLeases = leaseRepository.countActiveLeasesByCustomerId(leaseDto.getCustomerId());
        if (activeLeases >= 2) {
            throw new LeaseException("Customer cannot have more than 2 active leases");
        }
        
        // Get car
        Car car = carRepository.findById(leaseDto.getCarId())
                .orElseThrow(() -> new CarNotFoundException("Car not found with ID: " + leaseDto.getCarId()));
        
        // Check if car is available
        if (car.getStatus() != CarStatus.AVAILABLE) {
            throw new LeaseException("Car is not available for lease");
        }
        
        // Get customer
        Customer customer = customerRepository.findById(leaseDto.getCustomerId())
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found with ID: " + leaseDto.getCustomerId()));
        
        // Create lease
        Lease lease = new Lease();
        lease.setCar(car);
        lease.setCustomer(customer);
        lease.setStartDate(leaseDto.getStartDate());
        lease.setStatus(LeaseStatus.ACTIVE);
        lease.setTotalAmount(BigDecimal.ZERO); // Will be calculated when lease ends
        
        Lease savedLease = leaseRepository.save(lease);
        
        // Update car status
        car.setStatus(CarStatus.ON_LEASE);
        carRepository.save(car);
        
        log.info("Lease started successfully with ID: {}", savedLease.getId());
        
        return convertToDto(savedLease);
    }
    
    /**
     * End a lease
     */
    public LeaseDto endLease(Long leaseId) {
        log.info("Ending lease with ID: {}", leaseId);
        
        Lease lease = leaseRepository.findById(leaseId)
                .orElseThrow(() -> new LeaseException("Lease not found with ID: " + leaseId));
        
        if (lease.getStatus() != LeaseStatus.ACTIVE) {
            throw new LeaseException("Lease is not active");
        }
        
        // Calculate total amount
        LocalDateTime endDate = LocalDateTime.now();
        long days = ChronoUnit.DAYS.between(lease.getStartDate(), endDate);
        if (days == 0) days = 1; // Minimum 1 day charge
        
        BigDecimal totalAmount = lease.getCar().getDailyRate().multiply(BigDecimal.valueOf(days));
        
        // Update lease
        lease.setEndDate(endDate);
        lease.setTotalAmount(totalAmount);
        lease.setStatus(LeaseStatus.COMPLETED);
        
        Lease updatedLease = leaseRepository.save(lease);
        
        // Update car status to available
        Car car = lease.getCar();
        car.setStatus(CarStatus.AVAILABLE);
        carRepository.save(car);
        
        log.info("Lease ended successfully. Total amount: {}", totalAmount);
        
        return convertToDto(updatedLease);
    }
    
    /**
     * Get lease history for a customer
     */
    @Transactional(readOnly = true)
    public List<LeaseDto> getLeaseHistoryByCustomer(Long customerId) {
        log.info("Fetching lease history for customer: {}", customerId);
        
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found with ID: " + customerId));
        
        List<Lease> leases = leaseRepository.findByCustomer(customer);
        return leases.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    /**
     * Get lease history for a car
     */
    @Transactional(readOnly = true)
    public List<LeaseDto> getLeaseHistoryByCar(Long carId) {
        log.info("Fetching lease history for car: {}", carId);
        
        Car car = carRepository.findById(carId)
                .orElseThrow(() -> new CarNotFoundException("Car not found with ID: " + carId));
        
        List<Lease> leases = leaseRepository.findByCar(car);
        return leases.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    /**
     * Get active leases for a customer
     */
    @Transactional(readOnly = true)
    public List<LeaseDto> getActiveLeasesByCustomer(Long customerId) {
        log.info("Fetching active leases for customer: {}", customerId);
        
        List<Lease> leases = leaseRepository.findActiveLeasesByCustomerId(customerId);
        return leases.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    /**
     * Get all leases
     */
    @Transactional(readOnly = true)
    public List<LeaseDto> getAllLeases() {
        log.info("Fetching all leases");
        
        List<Lease> leases = leaseRepository.findAll();
        return leases.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    /**
     * Convert Lease entity to LeaseDto
     */
    private LeaseDto convertToDto(Lease lease) {
        LeaseDto dto = new LeaseDto();
        dto.setId(lease.getId());
        dto.setCarId(lease.getCar().getId());
        dto.setCustomerId(lease.getCustomer().getId());
        dto.setCarDetails(lease.getCar().getMake() + " " + lease.getCar().getModel() + 
                         " (" + lease.getCar().getLicensePlate() + ")");
        dto.setCustomerName(lease.getCustomer().getUser().getFullName());
        dto.setStartDate(lease.getStartDate());
        dto.setEndDate(lease.getEndDate());
        dto.setTotalAmount(lease.getTotalAmount());
        dto.setStatus(lease.getStatus());
        dto.setCreatedAt(lease.getCreatedAt());
        dto.setUpdatedAt(lease.getUpdatedAt());
        return dto;
    }
}
