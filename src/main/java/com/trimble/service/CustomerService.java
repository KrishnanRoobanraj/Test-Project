package com.trimble.service;

import com.trimble.dto.CustomerDto;
import com.trimble.entity.Customer;
import com.trimble.entity.User;
import com.trimble.enums.UserRole;
import com.trimble.exception.CustomerNotFoundException;
import com.trimble.repository.CustomerRepository;
import com.trimble.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service class for Customer operations
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class CustomerService {
    
    private final CustomerRepository customerRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    
    /**
     * Register a new customer
     */
    public CustomerDto registerCustomer(CustomerDto customerDto) {
        log.info("Registering new customer: {}", customerDto.getUsername());
        
        // Check if username already exists
        if (userRepository.existsByUsername(customerDto.getUsername())) {
            throw new RuntimeException("Username already exists: " + customerDto.getUsername());
        }
        
        // Check if email already exists
        if (userRepository.existsByEmail(customerDto.getEmail())) {
            throw new RuntimeException("Email already exists: " + customerDto.getEmail());
        }
        
        // Check if driver license already exists
        if (customerRepository.existsByDriverLicense(customerDto.getDriverLicense())) {
            throw new RuntimeException("Driver license already exists: " + customerDto.getDriverLicense());
        }
        
        // Create user
        User user = new User();
        user.setUsername(customerDto.getUsername());
        user.setPassword(passwordEncoder.encode(customerDto.getPassword()));
        user.setEmail(customerDto.getEmail());
        user.setFullName(customerDto.getFullName());
        user.setRole(UserRole.END_CUSTOMER);
        
        User savedUser = userRepository.save(user);
        
        // Create customer
        Customer customer = new Customer();
        customer.setUser(savedUser);
        customer.setPhoneNumber(customerDto.getPhoneNumber());
        customer.setAddress(customerDto.getAddress());
        customer.setDriverLicense(customerDto.getDriverLicense());
        
        Customer savedCustomer = customerRepository.save(customer);
        log.info("Customer registered successfully with ID: {}", savedCustomer.getId());
        
        return convertToDto(savedCustomer);
    }
    
    /**
     * Get customer by ID
     */
    @Transactional(readOnly = true)
    public CustomerDto getCustomerById(Long customerId) {
        log.info("Fetching customer with ID: {}", customerId);
        
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found with ID: " + customerId));
        
        return convertToDto(customer);
    }
    
    /**
     * Get customer by username
     */
    @Transactional(readOnly = true)
    public CustomerDto getCustomerByUsername(String username) {
        log.info("Fetching customer with username: {}", username);
        
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found with username: " + username));
        
        Customer customer = customerRepository.findByUser(user)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found with username: " + username));
        
        return convertToDto(customer);
    }
    
    /**
     * Get all customers
     */
    @Transactional(readOnly = true)
    public List<CustomerDto> getAllCustomers() {
        log.info("Fetching all customers");
        
        List<Customer> customers = customerRepository.findAll();
        return customers.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    /**
     * Convert Customer entity to CustomerDto
     */
    private CustomerDto convertToDto(Customer customer) {
        CustomerDto dto = new CustomerDto();
        dto.setId(customer.getId());
        dto.setUsername(customer.getUser().getUsername());
        dto.setEmail(customer.getUser().getEmail());
        dto.setFullName(customer.getUser().getFullName());
        dto.setPhoneNumber(customer.getPhoneNumber());
        dto.setAddress(customer.getAddress());
        dto.setDriverLicense(customer.getDriverLicense());
        dto.setCreatedAt(customer.getCreatedAt());
        dto.setUpdatedAt(customer.getUpdatedAt());
        return dto;
    }
}
