package enefit.rasmushaug.enefitpower.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import enefit.rasmushaug.enefitpower.components.JwtUtil;
import enefit.rasmushaug.enefitpower.model.Customer;
import enefit.rasmushaug.enefitpower.model.MeteringPoints;
import enefit.rasmushaug.enefitpower.service.CustomerService;
import enefit.rasmushaug.enefitpower.service.MeteringPointsService;

/**
 * REST Controller for managing metering points and associated consumptions for customers.
 * This controller provides endpoints for adding, retrieving, and managing metering points
 * and their consumption data, while ensuring proper authorization for each request.
 *
 * Endpoints:
 * - Add new metering points to a customer account.
 * - Retrieve metering points for a specific customer.
 * - Retrieve consumption data for a specific metering point.
 *
 * Authorization is enforced using JWT tokens to validate user access.
 */
@RestController
@RequestMapping("/api/customers")
public class MeteringPointsController {
    private static final Logger logger = LoggerFactory.getLogger(MeteringPointsController.class);

    @Autowired
    private MeteringPointsService meteringPointsService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private JwtUtil jwtUtil;

    /**
     * Adds a new metering point for a specific customer.
     *
     * @param customerId The ID of the customer to whom the metering point will be added.
     * @param meteringPoints The metering point details to be added.
     * @param token The authorization token provided in the request header.
     * @return ResponseEntity containing the saved metering point or an error message.
     *
     * @throws UnauthorizedAccessException if the logged-in user is not authorized to add a metering point for the given customer.
     */
    @PostMapping("/{customerId}/add-metering-point")
    public ResponseEntity<?> addMeteringPoint(@PathVariable Long customerId, @RequestBody MeteringPoints meteringPoints, @RequestHeader("Authorization") String token) {
        String loggedInUsername = jwtUtil.extractUsername(token.replace("Bearer ", ""));
        Customer loggedInCustomer = customerService.getCustomerByUsername(loggedInUsername);

        if (!loggedInCustomer.getCustomerId().equals(customerId)) {
            logger.error("Unauthorised user '{}' tried to create metering point for '{}'", loggedInUsername, customerId);
            return ResponseEntity.status(403).body("You are not authorized to add metering points.");
        }

        meteringPoints.setCustomer(loggedInCustomer);
        MeteringPoints savedMeteringPoint = meteringPointsService.saveMeteringPoint(meteringPoints);
        logger.info("Created new metering point for user '{}'", loggedInUsername);
        return ResponseEntity.ok(savedMeteringPoint);
    }

    /**
     * Retrieves the metering points for a specific customer.
     *
     * @param customerId The ID of the customer whose metering points are to be retrieved.
     * @param token The authorization token provided in the request header.
     * @return ResponseEntity containing the list of metering points or an error message.
     *
     * @throws UnauthorizedAccessException if the logged-in user is not authorized to view the metering points of the given customer.
     */
    @GetMapping("/{customerId}/get-metering-points")
    public ResponseEntity<?> getMeteringPoints(@PathVariable Long customerId, @RequestHeader("Authorization") String token) {
        String loggedInUsername = jwtUtil.extractUsername(token.replace("Bearer ", ""));
        Customer loggedInCustomer = customerService.getCustomerByUsername(loggedInUsername);

        if (!loggedInCustomer.getCustomerId().equals(customerId)) {
            logger.error("Unauthorised user '{}' tried to get metering points for user '{}'", loggedInCustomer, customerId);
            return ResponseEntity.status(403).body("You are not authorized to view metering points.");
        }
        List<MeteringPoints> meteringPoints = meteringPointsService.getMeteringPointsByCustomerId(customerId);
        logger.info("User '{}' read metering point '{}'", loggedInUsername, meteringPoints);
        return ResponseEntity.ok(meteringPoints);
    }
}
