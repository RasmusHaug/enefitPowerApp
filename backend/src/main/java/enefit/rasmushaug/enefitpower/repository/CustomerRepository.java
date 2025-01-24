package enefit.rasmushaug.enefitpower.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import enefit.rasmushaug.enefitpower.model.Customer;

/**
 * Repository interface for accessing customer data from the database.
 *
 * This interface extends {@link JpaRepository} and provides CRUD operations and custom queries
 * for the `Customer` entity. It allows the application to perform database operations
 * such as saving, updating, deleting, and finding `Customer` entities.
 *
 * The interface includes a custom method to find a customer by their username.
 *
 * <p>Usage:</p>
 * <ul>
 *   <li>Spring Data JPA automatically implements the CRUD operations provided by {@link JpaRepository}.</li>
 *   <li>Custom query methods can be added to the interface, such as {@link #findByUsername(String)}.</li>
 * </ul>
 */
@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    /**
     * Finds a customer by their username.
     *
     * This method allows retrieving a customer, based on the provided username.
     * If the customer exists in the database, it returns the corresponding `Customer` entity.
     * If no customer with that username is found it returns {@code null}.
     *
     * @param username The username of the customer to be retrieved.
     * @return Customer The `Customer` entity associated with the provided username, or {@code null} if not found.
     */
    Customer findByUsername(String username);
}
