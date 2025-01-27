package enefit.rasmushaug.enefitpower.model;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

/**
 * Entity class representing a customer in the system.
 * <p>
 * This class maps to the "customer" table in the database and is used to store customer information:
 * first name, last name, username, and password. The `customerId` is the primary key and is auto-generated.
 * </p>
 * <p>
 * The username is unique, and it is used for customer authentication during login. Passwords are stored encoded.
 * The encoding process is handled by Service class `CustomerService.java`.
 * </p>
 */
@Entity
@Table(name = "customer")
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long customerId;

    private String firstName;
    private String lastName;

    @Column(unique = true)
    private String username;
    private String password;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MeteringPoints> meteringPoints;

    /**
     * Returns a string representation of the customer object.
     *
     * This method provides a simplified version of the customer details, excluding the password.
     * Primarily meant to be used for logging and debugging purposes.
     *
     * @return A string representing the customer object, including customerId, firstName, lastName, and username.
     */
    @Override
    public String toString() {
        return "Customer{" +
            "customerId=" + customerId +
            ", firstName='" + firstName + '\'' +
            ", lastName='" + lastName + '\'' +
            ", username='" + username + '\'' +
            '}';
    }

    // GETTERS
    /**
     * Gets the unique ID of the customer.
     *
     * @return The customer ID.
     */
    public Long getCustomerId() {
        return customerId;
    }

    /**
     * Gets the first name of the customer.
     *
     * @return The first name of the customer.
     */
    public String getFirstName() {
        return firstName;
    }

    /**¬
     * Gets the last name of the customer.
     *
     * @return The last name of the customer.
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Gets the password of the customer.
     *
     * @return The password of the customer.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Gets the username of the customer.
     *
     * @return The username of the customer.
     */
    public String getUsername() {
        return username;
    }

    public List<MeteringPoints> getMeteringPoints() {
        return meteringPoints;
    }

    // SETTERS
    /**
     * Sets the unique ID of the customer.
     *
     * @param customerId The customer ID to be set.
     */
    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    /**
     * Sets the first name of the customer.
     *
     * @param firstName The first name to be set for the customer.
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Sets the last name of the customer.
     *
     * @param lastName The last name to be set for the customer.
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Sets the password for the customer.
     *
     * @param password The password to be set for the customer.
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Sets the username for the customer.
     *
     * @param username The username to be set for the customer.
     */
    public void setUsername(String username) {
        this.username = username;
    }

    public void setMeteringPoints(List<MeteringPoints> meteringPoints) {
        this.meteringPoints = meteringPoints;
    }
}
