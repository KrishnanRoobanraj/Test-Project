package com.trimble.repository;

import com.trimble.entity.Car;
import com.trimble.entity.User;
import com.trimble.enums.CarStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Car entity
 */
@Repository
public interface CarRepository extends JpaRepository<Car, Long> {
    
    List<Car> findByOwner(User owner);
    
    List<Car> findByStatus(CarStatus status);
    
    Optional<Car> findByLicensePlate(String licensePlate);
    
    @Query("SELECT c FROM Car c WHERE c.owner.id = :ownerId")
    List<Car> findByOwnerId(@Param("ownerId") Long ownerId);
    
    @Query("SELECT c FROM Car c WHERE c.status = 'AVAILABLE'")
    List<Car> findAvailableCars();
    
    boolean existsByLicensePlate(String licensePlate);
}
