package com.trimble.repository;

import com.trimble.entity.Car;
import com.trimble.entity.Customer;
import com.trimble.entity.Lease;
import com.trimble.enums.LeaseStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for Lease entity
 */
@Repository
public interface LeaseRepository extends JpaRepository<Lease, Long> {
    
    List<Lease> findByCustomer(Customer customer);
    
    List<Lease> findByCar(Car car);
    
    List<Lease> findByStatus(LeaseStatus status);
    
    @Query("SELECT l FROM Lease l WHERE l.customer.id = :customerId AND l.status = 'ACTIVE'")
    List<Lease> findActiveLeasesByCustomerId(@Param("customerId") Long customerId);
    
    @Query("SELECT l FROM Lease l WHERE l.car.id = :carId AND l.status = 'ACTIVE'")
    List<Lease> findActiveLeasesByCarId(@Param("carId") Long carId);
    
    @Query("SELECT COUNT(l) FROM Lease l WHERE l.customer.id = :customerId AND l.status = 'ACTIVE'")
    long countActiveLeasesByCustomerId(@Param("customerId") Long customerId);
}
