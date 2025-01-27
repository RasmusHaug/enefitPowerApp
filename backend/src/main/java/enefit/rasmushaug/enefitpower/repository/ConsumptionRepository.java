package enefit.rasmushaug.enefitpower.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import enefit.rasmushaug.enefitpower.model.Consumption;

/**
 * Repository interface for accessing Consumption data from the database.
 *
 * This interface extends {@link JpaRepository}.
 * The interface includes two methods:
 * Interface can return List of Customer Consumption data using Customer ID value or
 * the interface can return List of a Metering Point Consumption data.
 */
public interface ConsumptionRepository extends JpaRepository<Consumption, Long> {
    List<Consumption> findByMeteringPointCustomerCustomerId(Long customerId);

    List<Consumption> findByMeteringPointMeteringPointId(Long meteringPointId);
}