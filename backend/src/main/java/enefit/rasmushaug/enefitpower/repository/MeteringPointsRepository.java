package enefit.rasmushaug.enefitpower.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import enefit.rasmushaug.enefitpower.model.MeteringPoints;

import java.util.List;

/**
 * Repository interface for accessing Metering Point data from the database
 *
 * This interface extends {@link Jpa Repository}.
 * The interface includes a method to find Metering point by Customer ID value.
 */
public interface MeteringPointsRepository extends JpaRepository<MeteringPoints, Long> {
    List<MeteringPoints> findByCustomerCustomerId(Long customerId);
}
