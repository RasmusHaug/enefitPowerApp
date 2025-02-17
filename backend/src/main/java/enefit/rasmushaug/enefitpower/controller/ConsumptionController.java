package enefit.rasmushaug.enefitpower.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import enefit.rasmushaug.enefitpower.components.JwtUtil;
import enefit.rasmushaug.enefitpower.dto.ConsumptionDTO;
import enefit.rasmushaug.enefitpower.model.Consumption;
import enefit.rasmushaug.enefitpower.model.Consumption.AmountUnit;
import enefit.rasmushaug.enefitpower.model.Customer;
import enefit.rasmushaug.enefitpower.model.MeteringPoints;
import enefit.rasmushaug.enefitpower.service.ConsumptionService;
import enefit.rasmushaug.enefitpower.service.CustomerService;
import enefit.rasmushaug.enefitpower.service.MeteringPointsService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;


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
     * This API ensures that only the customer who owns the Metering Point can add new Consumption records.
     *
     * @param customerId The customer ID used to validate the customer making the request.
     * @param meteringPointId The metering point ID associated with the consumption being added.
     * @param consumption The Consumption data consisting of the metering point, amount of consumption, and the time of consumption.
     * @param token Authorization token, JWT Session token given to the customer client to verify that the customer has the rights to write this data.
     * @return ResponseEntity containing the Consumption if the customer is authorized to add it, or an error message with status code.
     */
    @PostMapping("/{customerId}/{meteringPointId}/add-consumption")
    public ResponseEntity<?> addConsumption(@PathVariable long customerId, @PathVariable long meteringPointId, @RequestBody Consumption consumption, @RequestHeader("Authorization") String token) {
        String loggedInUsername = jwtUtil.extractUsername(token.replace("Bearer ", ""));
        if (checkAuthorization(token, customerId) == null) {
            MeteringPoints meteringPoint = meteringPointsService.getMeteringPointsByCustomerId(customerId).stream()
                .filter(mp -> mp.getMeteringPointId() == meteringPointId)
                .findFirst()
                .orElse(null);

            if (meteringPoint == null) {
                logger.error("Metering point is null for customer '{}'", customerId);
                return ResponseEntity.status(400).body("Metering point is required.");
            }

            consumption.setMeteringPoint(meteringPoint);

            Consumption savedConsumption = consumptionService.saveConsumption(consumption);

            logger.info("User '{}' added consumption for customer '{}'", loggedInUsername, customerId);
            return ResponseEntity.ok(savedConsumption);
        } else {
            logger.error("Customer '{}' doesn't have authorisation to add new consumption", customerId);
            return ResponseEntity.status(400).body("Metering point is required.");
        }
    }

    /**
     * Obtains Consumption for a specific Metering Point for a Customer
     *
     * This API will allow the client to obtain Consumption data for a specific Metering Point
     * of a Customer.
     *
     * This method ensures that the Customer making the request is the correct Customer
     * and verifies that the specified Metering Point belongs to that Customer.
     *
     * This ensures that only the customer who owns the Metering Point can view the consumption records.
     *
     * @param customerId The customer ID used to verify customer ownership of the Metering Point and obtain consumption data.
     * @param meteringPointId The ID of the Metering Point for which the consumption data is being requested.
     * @param token Authorization token, JWT Session token given to the customer client to verify the customer has the rights to view this data.
     * @return ResponseEntity of Consumption data if the Customer can view the data. Returns an error message if anything fails.
     */
    @GetMapping("/{customerId}/{meteringPointId}/get-consumptions")
    public ResponseEntity<?> getConsumptions( @PathVariable long customerId, @PathVariable long meteringPointId, @RequestHeader("Authorization") String token) {
        String loggedInUsername = jwtUtil.extractUsername(token.replace("Bearer ", ""));
        if (checkAuthorization(token, customerId) == null) {
            MeteringPoints meteringPoint = meteringPointsService.getMeteringPointsById(meteringPointId);
            if (meteringPoint == null || !meteringPoint.getCustomer().getCustomerId().equals(customerId)) {
                logger.error("Unauthorized access: User '{}' tried to fetch consumptions for an unauthorized metering point '{}'", loggedInUsername, meteringPointId);
                return ResponseEntity.status(403).body("You are not authorized to view consumptions for this metering point.");
            }

            List<ConsumptionDTO> dailyConsumptions = consumptionService.sumDailyConsumptions(meteringPoint.getConsumptionRecords());

            logger.info("User '{}' fetched consumptions for location '{}'", loggedInUsername, meteringPointId);
            return ResponseEntity.ok(dailyConsumptions);
        } else {
            logger.error("Customer '{}' doesn't have Authorisation to get consumption data.", loggedInUsername);
            return ResponseEntity.status(403).body("You are not authorized to view consumptions for this metering point.");
        }
    }

    @GetMapping("/{customerId}/get-consumptions")
        public ResponseEntity<?> getCustomerConsumptions(@PathVariable long customerId, @RequestHeader("Authorization") String token) {
            String loggedInUsername = jwtUtil.extractUsername(token.replace("Bearer ", ""));
            if (checkAuthorization(token, customerId) == null) {
                return ResponseEntity.ok(consumptionService.getCustomerYearlyConsumption(customerId));
            } else {
                logger.error("Customer '{}' doesn't have Authorisation to get consumption data.", loggedInUsername);
                return ResponseEntity.status(403).body("You are not authorized to view consumptions for this metering point.");
            }
        }

        public ResponseEntity<String> checkAuthorization(String token, long customerId) {
        String loggedInUsername = jwtUtil.extractUsername(token.replace("Bearer ", ""));
        Customer loggedInCustomer = customerService.getCustomerByUsername(loggedInUsername);

        if (loggedInCustomer == null || !loggedInCustomer.getCustomerId().equals(customerId)) {
            logger.error("Unauthorized user '{}' tried to access data for customer '{}'", loggedInUsername, customerId);
            return ResponseEntity.status(500).body("You are not authorized to access this customer's data.");
        }
        return null;
    }
}
