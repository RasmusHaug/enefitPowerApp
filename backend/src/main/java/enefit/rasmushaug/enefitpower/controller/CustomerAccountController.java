package enefit.rasmushaug.enefitpower.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import enefit.rasmushaug.enefitpower.dto.CustomerResponse;
import enefit.rasmushaug.enefitpower.dto.CustomerResponseError;
import enefit.rasmushaug.enefitpower.dto.LoginRequest;
import enefit.rasmushaug.enefitpower.model.Customer;
import enefit.rasmushaug.enefitpower.service.CustomerService;
import jakarta.servlet.http.HttpSession;

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
    public ResponseEntity<CustomerResponse> registerCustomer(@RequestBody Customer customer) {
        try {
            Customer createdCustomer = customerService.registerCustomer(customer);
            if (createdCustomer == null) {
                logger.error("Customer creation failed for: {}", customer);
                return ResponseEntity.status(500).body(new CustomerResponseError("Registration Failed"));
            }
            return ResponseEntity.ok(new CustomerResponse(createdCustomer));
        } catch (IllegalArgumentException e) {
            logger.error("Error registering customer: {}", e.getMessage());
            return ResponseEntity.status(400).body(new CustomerResponseError(e.getMessage()));
        } catch (Exception e) {
            logger.error("Unexpected error during registration: {}", e.getMessage(), e);
            return ResponseEntity.status(500).body(new CustomerResponseError("Unexpected Error: " + e.getMessage()));
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
    public ResponseEntity<CustomerResponse> loginCustomer(@RequestBody LoginRequest loginRequest, HttpSession session) {
        Customer customer = customerService.login(loginRequest.getUsername(), loginRequest.getPassword());
        if (customer != null) {
            CustomerResponse sessionCustomer = new CustomerResponse(customer);
            session.setAttribute("user", sessionCustomer.getCustomerId());
            sessionCustomer.setSessionId(session.getId());
            logger.info("User logged in successfully: {}", sessionCustomer);
            return ResponseEntity.ok(sessionCustomer);
        }
        return ResponseEntity.status(401).body(null);
    }

    /**
     * Logs out an existing customer.
     *
     * This API endpoint allows customers to log out by invalidating their session.
     *
     * @param session The HTTP session to invalidate.
     * @return ResponseEntity<String> A responseEntity with a logout success message.
     *         Returns HTTP 200 OK on successful logout.
     */
    @PostMapping("/logout")
    public ResponseEntity<String> logoutCustomer(@RequestBody String sessionId, HttpSession session) {
        String cleanedSessionId = sessionId.replace("\"", "").trim();
        try {
            if (cleanedSessionId.equals(session.getId())) {
                session.invalidate();
                logger.info("Successfully logged out customer with session ID {}", cleanedSessionId);
                return ResponseEntity.ok("Logged out successfully.");
            }
            logger.warn("Invalid session ID provided for logout: {}", cleanedSessionId);
            return ResponseEntity.status(401).body("Invalid session ID.");
        } catch (Exception e) {
            logger.error("Error during logout: {}", e.getMessage(), e);
            return ResponseEntity.status(500).body("Unexpected error during logout.");
        }
    }


    /**
     * Custom handler for username already taken exception
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<CustomerResponseError> handleUsernameTakenException(IllegalArgumentException ex) {
        return ResponseEntity.status(400).body(new CustomerResponseError(ex.getMessage()));
    }
}
