package enefit.rasmushaug.enefitpower.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import enefit.rasmushaug.enefitpower.components.JwtUtil;
import enefit.rasmushaug.enefitpower.model.Customer;
import enefit.rasmushaug.enefitpower.repository.CustomerRepository;

/**
 * Service layer for managing customer operations: registration and login.
 *
 * This service provides methods to register a new customer, ensuring that the username is unique,
 * and to authenticate an existing customer during login by validating the provided username and password.
 *
 * The class also handles password encoding and logging of successful and failed registration and login attempts.
 */
@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private JwtUtil jwtUtil;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private static final Logger logger = LoggerFactory.getLogger(CustomerService.class);

    /**
     * Registers a new customer with unique username validation.
     *
     * Verifies if the username is already taken or not, if username is available, the set password is encoded using BCrypt, and customer is saved to the database.
     * Logs successfull registration of user to logger for user verification.
     *
     * @param customer The customer to be registered.
     * @return The saved customer with the encoded password.
     * @throws IllegalArgumentException if the username is already taken.
     */
    public Customer registerCustomer(Customer customer) {
        if (customerRepository.findByUsername(customer.getUsername()) != null) {
            throw new IllegalArgumentException("Username is already taken");
        }

        customer.setPassword(passwordEncoder.encode(customer.getPassword()));
        Customer savedCustomer = customerRepository.save(customer);
        // Logging customer registration to Logger
        logger.info("User Registered successfully: {}", savedCustomer);
        return savedCustomer;
    }

    /**
     * Logs excisting customer to their account after verifying account details are correct.
     *
     * @param username The customer username to log in.
     * @param password The customer password to log in.
     * @return Customer The Customer full data if sucessfully logged in.
     *         Returns null if the username and/or password don't match to data in database.
     */
    public String login(String username, String password) {
        Customer customer = customerRepository.findByUsername(username);
        if (customer != null && passwordEncoder.matches(password, customer.getPassword())) {
            return jwtUtil.generatedToken(username);
        }
        if (customer == null) {
            logger.info("Failed login attempt, username not found {}", username);
        } else {
            logger.info("Failed login for {}", customer.getUsername());
        }
        return null;
    }

    /**
     * Getter to find Customer using customer Username.
     *
     * @param username The username used to search for excisting customers
     * @return Customer full data if it excists.
     */
    public Customer getCustomerByUsername(String username) {
        return customerRepository.findByUsername(username);
    }

    /**
     * Getter to find Customer using Customer ID value.
     *
     * @param customerId The Customer ID used to search for excisitn customers
     * @return Customer object if excists that can be used to access Customer specific details.
     */
    public Customer getCustomerById(Long customerId) {
        return customerRepository.findById(customerId).orElseThrow(() -> new IllegalArgumentException("Customer not found with ID: " + customerId));
    }
}
