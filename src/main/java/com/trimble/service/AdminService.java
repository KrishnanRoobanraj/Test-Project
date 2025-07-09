package com.trimble.service;

import com.trimble.dto.CarDto;
import com.trimble.dto.CustomerDto;
import com.trimble.dto.LeaseDto;
import com.trimble.enums.CarStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service class for Admin operations
 * Admin can perform all operations for cars, customers, and leases
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class AdminService {
    
    private final CarService carService;
    private final CustomerService customerService;
    private final LeaseService leaseService;
    
    /**
     * Get all cars in the system
     */
    @Transactional(readOnly = true)
    public List<CarDto> getAllCars() {
        log.info("Admin fetching all cars");
        return carService.getAllCars();
    }
    
    /**
     * Get all customers in the system
     */
    @Transactional(readOnly = true)
    public List<CustomerDto> getAllCustomers() {
        log.info("Admin fetching all customers");
        return customerService.getAllCustomers();
    }
    
    /**
     * Get all leases in the system
     */
    @Transactional(readOnly = true)
    public List<LeaseDto> getAllLeases() {
        log.info("Admin fetching all leases");
        return leaseService.getAllLeases();
    }
    
    /**
     * Update car status
     */
    public CarDto updateCarStatus(Long carId, CarStatus status) {
        log.info("Admin updating car status for ID: {} to {}", carId, status);
        return carService.updateCarStatus(carId, status);
    }
    
    /**
     * End any lease
     */
    public LeaseDto endLease(Long leaseId) {
        log.info("Admin ending lease with ID: {}", leaseId);
        return leaseService.endLease(leaseId);
    }
    
    /**
     * Get lease history for any car
     */
    @Transactional(readOnly = true)
    public List<LeaseDto> getLeaseHistoryByCar(Long carId) {
        log.info("Admin fetching lease history for car: {}", carId);
        return leaseService.getLeaseHistoryByCar(carId);
    }
    
    /**
     * Get lease history for any customer
     */
    @Transactional(readOnly = true)
    public List<LeaseDto> getLeaseHistoryByCustomer(Long customerId) {
        log.info("Admin fetching lease history for customer: {}", customerId);
        return leaseService.getLeaseHistoryByCustomer(customerId);
    }
}
