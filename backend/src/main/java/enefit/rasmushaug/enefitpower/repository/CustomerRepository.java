package enefit.rasmushaug.enefitpower.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import enefit.rasmushaug.enefitpower.model.Customer;

/**
 * Repository interface for accessing customer data from the database.
 *
 * This interface extends {@link JpaRepository}.
 * The interface includes a method to find a customer by their username.
 */
@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Customer findByUsername(String username);
}
