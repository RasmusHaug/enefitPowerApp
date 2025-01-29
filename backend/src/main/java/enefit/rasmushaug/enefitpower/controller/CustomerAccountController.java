package enefit.rasmushaug.enefitpower.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import enefit.rasmushaug.enefitpower.dto.CustomerDTO;
import enefit.rasmushaug.enefitpower.model.Customer;
import enefit.rasmushaug.enefitpower.repository.CustomerRepository;
import enefit.rasmushaug.enefitpower.service.CustomerService;

/**
 * Controller for handling customer account operations.
 *
 * This class provides endpoints for customer registration and login. It interacts with the
 * CustomerService to perform necessary logic and return appropriate responses.
 *
 * The `registerCustomer` method allows new customers to register by providing their details,
 * while the `loginCustomer` method allows existing customers to log in by providing their credentials.
 */
@RestController
@RequestMapping("/api/customers")
public class CustomerAccountController {
    private static final Logger logger = LoggerFactory.getLogger(CustomerAccountController.class);

    @Autowired
    private CustomerService customerService;

    @Autowired
    private CustomerRepository customerRepository;

    /**
     *
     * Registers a new customer
     *
     * This API endpoint allows clients (Vite front-end) to register a new customer by providing the customer's details
     * in the response body. The customers password will be encoded before being saved to the database.
     *
     * @param customer The customer details to be registered.
     * @return ResponseEntity<CustomerResponse> A ResponseEntity containing the registered customer details in the response body.
     *              Returns HTTP status 200 OK on successful registration.
     *              Returns HTTP status 400 on IllegalArgumentException with error message.
     */
    @PostMapping("/register")
    public ResponseEntity<?> registerCustomer(@RequestBody Customer customer) {
        try {
            if (Boolean.TRUE.equals(customerRepository.existsByUsername(customer.getUsername()))) {
                logger.error("Username '{}' already taken.", customer.getUsername());
                return ResponseEntity.status(500).body("Username already taken");
            } else {
                Customer createdCustomer = customerService.registerCustomer(customer);
                if (createdCustomer == null) {
                    logger.error("Customer creation failed for: {}", customer);
                    return ResponseEntity.status(500).body("Registration Failed");
                }
                return ResponseEntity.ok(new CustomerDTO(createdCustomer));
            }
        } catch (IllegalArgumentException e) {
            logger.error("Error registering customer: {}", e.getMessage());
            return ResponseEntity.status(400).body(e.getMessage());
        } catch (Exception e) {
            logger.error("Unexpected error during registration: {}", e.getMessage(), e);
            return ResponseEntity.status(500).body("Unexpected Error: " + e.getMessage());
        }
    }

    /**
     *
     * Logs in an existing customer
     *
     * This API endpoint allows customers to log in by providing their username and password.
     * If the credentials are valid, the system returns a success message. Otherwise, it returns an error message
     * with a 401 Unauthorized status.
     *
     * @param loginRequest The login request containing the username and password
     * @return ResponseEntity<String> A responseEntity containing the login status message in the response body.
     *              Returns HTTP 200 OK on successful login,
     *              and 401 Unauthorized if the credentials are invalid.
     */
    @PostMapping("/login")
    public ResponseEntity<?> loginCustomer(@RequestBody Map<String, String> credentials) {
        String username = credentials.get("username");
        String password = credentials.get("password");
        String jwtToken = customerService.login(username, password);
        if (jwtToken != null) {
            Customer customer = customerRepository.findByUsername(username);
            if (customer != null) {
                CustomerDTO sessionCustomer = new CustomerDTO(customer, jwtToken);
                logger.info("User logged in successfully: {}", sessionCustomer);
                return ResponseEntity.ok(sessionCustomer);
            } else {
                logger.error("Failed to located user using credentials: '{}'", credentials);
                return ResponseEntity.status(500).body("Faild to find customer");
            }
        } else {
            logger.error("Failed to create JWT Token for Customer using credentials '{}'", credentials);
            return ResponseEntity.status(401).body("Login failed.");
        }
    }

    /**
     * Method to just return a String the customer has logged out.
     * Client side is responsible for deleting the session token.
     *
     * @return ResponseEntity<String> A responseEntity with a logout success message.
     *         Returns HTTP 200 OK on successful logout.
     */
    @PostMapping("/logout")
    public ResponseEntity<String> logoutCustomer() {
        // TODO: Add credentials to parameter in order to display what user logs out using logger.
        return ResponseEntity.ok("Logged out successfully.");
    }
}
