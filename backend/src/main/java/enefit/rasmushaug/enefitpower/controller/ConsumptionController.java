package enefit.rasmushaug.enefitpower.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import enefit.rasmushaug.enefitpower.components.JwtUtil;
import enefit.rasmushaug.enefitpower.model.Consumption;
import enefit.rasmushaug.enefitpower.model.Customer;
import enefit.rasmushaug.enefitpower.model.MeteringPoints;
import enefit.rasmushaug.enefitpower.service.ConsumptionService;
import enefit.rasmushaug.enefitpower.service.CustomerService;
import enefit.rasmushaug.enefitpower.service.MeteringPointsService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Controller for handling Consumption operations
 * 
 * This class provides endpoints to add or get all consumption data for a specific customer.
 * 
 * The `addConsumption` method allows customer to add Consumption to a specific Metering Point.
 * The `getConsumption` method allows customer to get Consumption data for a specific Customer.
 */
@RestController
@RequestMapping("/api/customers")
public class ConsumptionController {
    private static final Logger logger = LoggerFactory.getLogger(ConsumptionController.class);

    @Autowired
    private ConsumptionService consumptionService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private MeteringPointsService meteringPointsService;

    @Autowired
    private JwtUtil jwtUtil;

    /**
     * Adds Consumption for a Customer to a specified Metering Point.
     * 
     * This API will allow customer to add new consumption records.
     * 
     * This method makes sure that the Customer making the request to add Consumption data
     * is the correct Customer and Verifies that the Metering Point does belong to that Customer.
     * 
     * This ensures that only the customer who owns the Metering Point can add new Consumption records.
     * 
     * @param customerId The customer ID value used to obtain customer data to add a new Consumption record.
     * @param consumption The Consumption data consisitng of the metering point and how much electricity and when it was used.
     * @param token Autorization token, JWT Session token given to the customer client to verify that the customer has the rights to write this data.
     * @return ResponseEntity of Consumption if the Customer can write the data. Returns HTML OK message to client.
     *         ResponseEntity of String if anything fails. Sends a Status code and message detailing the issue.
     */
    @PostMapping("/{customerId}/add-consumptions")
    public ResponseEntity<?> addConsumption(@PathVariable Long customerId, @RequestBody Consumption consumption, @RequestHeader("Authorization") String token) {
        String loggedInUsername = jwtUtil.extractUsername(token.replace("Bearer ", ""));
        Customer loggedInCustomer = customerService.getCustomerByUsername(loggedInUsername);

        if (!loggedInCustomer.getCustomerId().equals(customerId)) {
            logger.error("Unauthorized user '{}' with ID '{}' tried to add consumption for customer '{}'", loggedInUsername,loggedInCustomer.getCustomerId(), customerId);
            return ResponseEntity.status(403).body("You are not authorized to add consumption.");
        }

        logger.info("Consumption Object sent: {}", consumption);
        MeteringPoints meteringPoint = consumption.getMeteringPoint();
        if (meteringPoint == null) {
            logger.error("Metering point is null for customer '{}'", customerId);
            return ResponseEntity.status(400).body("Metering point is required.");
        }

        List<MeteringPoints> customerMeteringPoints = meteringPointsService.getMeteringPointsByCustomerId(customerId);
        boolean isValidMeteringPoint = customerMeteringPoints.stream()
            .anyMatch(mp -> mp.getMeteringPointId() == meteringPoint.getMeteringPointId());

        if (!isValidMeteringPoint) {
            logger.error("Metering point '{}' does not belong to customer '{}'", meteringPoint.getMeteringPointId(), customerId);
            return ResponseEntity.status(400).body("Invalid metering point.");
        }

        consumption.setMeteringPoint(meteringPoint);

        Consumption savedConsumption = consumptionService.saveConsumption(consumption);

        logger.info("User '{}' added consumption for customer '{}'", loggedInUsername, customerId);
        return ResponseEntity.ok(savedConsumption);
    }

    /**
     * Obtains Consumption for a Customer
     * 
     * This API will allow the client to obtain Consumption data for a Customer.
     * 
     * This method makes sure that the Customer making the request to get the Consumption data
     * is the correct Customer and Verifies that the Metering Point does belong to that Customer.
     * 
     * This ensures that only the customer who owns the Metering Point can add new Consumption records.
     * 
     * @param customerId The customer ID value used to obtain customer data to get Consumption Records
     * @param token Autorization token, JWT Session token given to the customer client to verify that the customer has the rights to write this data.
     * @return ResponseEntity of Consumption if the Customer can write the data. Returns HTML OK message to client.
     *         ResponseEntity of String if anything fails. Sends a Status code and message detailing the issue.
     */
    @GetMapping("/{customerId}/get-consumptions")
    public ResponseEntity<?> getConsumptions( @PathVariable Long customerId, @RequestHeader("Authorization") String token) {
        String loggedInUsername = jwtUtil.extractUsername(token.replace("Bearer ", ""));
        Customer loggedInCustomer = customerService.getCustomerByUsername(loggedInUsername);

        if (!loggedInCustomer.getCustomerId().equals(customerId)) {
            logger.error("Unauthorized user '{}' tried to get consumptions for customer '{}'", loggedInUsername, customerId);
            return ResponseEntity.status(403).body("You are not authorized to view consumptions.");
        }

        List<Consumption> consumptions = consumptionService.getConsumptionsByCustomerId(customerId);
        logger.info("User '{}' fetched consumptions for location '{}'", loggedInUsername, consumptions);
        return ResponseEntity.ok(consumptions);
    }
}
