package com.trimble.repository;

import com.trimble.entity.Customer;
import com.trimble.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository interface for Customer entity
 */
@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    
    Optional<Customer> findByUser(User user);
    
    Optional<Customer> findByDriverLicense(String driverLicense);
    
    boolean existsByDriverLicense(String driverLicense);
}
