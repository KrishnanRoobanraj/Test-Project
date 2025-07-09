package com.trimble.controller;

import com.trimble.dto.ApiResponse;
import com.trimble.dto.CustomerDto;
import com.trimble.service.CustomerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

/**
 * REST Controller for Customer operations
 */
@RestController
@RequestMapping("/api/customers")
@RequiredArgsConstructor
@Slf4j
public class CustomerController {
    
    private final CustomerService customerService;
    
    /**
     * Register a new customer
     */
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<CustomerDto>> registerCustomer(
            @Valid @RequestBody CustomerDto customerDto) {
        
        log.info("Customer registration request received for username: {}", customerDto.getUsername());
        
        CustomerDto registeredCustomer = customerService.registerCustomer(customerDto);
        
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Customer registered successfully", registeredCustomer));
    }
    
    /**
     * Get customer by ID
     */
    @GetMapping("/{customerId}")
    public ResponseEntity<ApiResponse<CustomerDto>> getCustomerById(@PathVariable Long customerId) {
        log.info("Fetching customer with ID: {}", customerId);
        
        CustomerDto customer = customerService.getCustomerById(customerId);
        
        return ResponseEntity.ok(ApiResponse.success("Customer fetched successfully", customer));
    }
    
    /**
     * Get customer by username
     */
    @GetMapping("/username/{username}")
    public ResponseEntity<ApiResponse<CustomerDto>> getCustomerByUsername(@PathVariable String username) {
        log.info("Fetching customer with username: {}", username);
        
        CustomerDto customer = customerService.getCustomerByUsername(username);
        
        return ResponseEntity.ok(ApiResponse.success("Customer fetched successfully", customer));
    }
}