package enefit.rasmushaug.enefitpower.controller;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import enefit.rasmushaug.enefitpower.model.Consumption;
import enefit.rasmushaug.enefitpower.model.Customer;
import enefit.rasmushaug.enefitpower.model.MeteringPoints;
import enefit.rasmushaug.enefitpower.service.ConsumptionService;
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
    private ConsumptionService consumptionService;

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
    @PostMapping("/{customerId}/add-metering-points")
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

    /**
     * Retrieves the consumption data for a specific metering point of a customer.
     *
     * @param customerId The ID of the customer who owns the metering point.
     * @param meteringPointId The ID of the metering point for which consumption data is requested.
     * @param token The authorization token provided in the request header.
     * @return ResponseEntity containing the list of consumption data or an error message.
     *
     * @throws UnauthorizedAccessException if the logged-in user is not authorized to view the consumption data for the given metering point.
     * @throws NotFoundException if no consumption data is found for the specified metering point.
     */
    @GetMapping("/{customerId}/metering-points/{meteringPointId}/consumptions")
    public ResponseEntity<?> getConsumptionsForMeteringPoint(
            @PathVariable Long customerId,
            @PathVariable Long meteringPointId,
            @RequestHeader("Authorization") String token) {

        String loggedInUsername = jwtUtil.extractUsername(token.replace("Bearer ", ""));
        Customer loggedInCustomer = customerService.getCustomerByUsername(loggedInUsername);

        if (!loggedInCustomer.getCustomerId().equals(customerId)) {
            logger.error("Unauthorized user '{}' tried to fetch consumptions for metering point '{}' for customer '{}'", loggedInUsername, meteringPointId, customerId);
            return ResponseEntity.status(403).body("You are not authorized to view consumptions.");
        }

        try {
            List<Consumption> consumptions = consumptionService.getConsumptionsByMeteringPointId(meteringPointId);
            if (consumptions == null || consumptions.isEmpty()) {
                logger.warn("No consumptions found for metering point '{}' of customer '{}'", meteringPointId, customerId);
                return ResponseEntity.ok(Collections.emptyList());
            }

            List<Consumption> summedConsumptions = sumConsumptionsByDate(consumptions);

            logger.info("User '{}' fetched and processed consumptions for metering point '{}' of customer '{}'", loggedInUsername, meteringPointId, customerId);
            return ResponseEntity.ok(summedConsumptions);

        } catch (Exception e) {
            logger.error("An error occurred while retrieving consumptions for metering point '{}' of customer '{}'", meteringPointId, customerId, e);
            return ResponseEntity.status(500).body("An error occurred while fetching consumption data. Please try again later.");
        }
    }

    /**
     * Sums the consumptions by date.
     *
     * @param consumptions The list of consumptions to process.
     * @return A list of consumptions with summed amounts for each date.
     */
    private List<Consumption> sumConsumptionsByDate(List<Consumption> consumptions) {
        Map<String, Double> groupedConsumptions = new HashMap<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        logger.info("Starting to group and sum consumptions by date...");

        for (Consumption consumption : consumptions) {
            String date = dateFormat.format(consumption.getConsumptionTime());
            double amount = consumption.getAmount();
            groupedConsumptions.put(date, groupedConsumptions.getOrDefault(date, 0.0) + amount);
        }

        logger.info("Finished processing consumptions. Grouped consumptions by date: {}", groupedConsumptions);

        List<Consumption> result = new ArrayList<>();
        for (Map.Entry<String, Double> entry : groupedConsumptions.entrySet()) {
            Consumption consumption = new Consumption();
            try {
                String dateString = entry.getKey() + "T00:00:00Z";
                Timestamp timestamp = Timestamp.valueOf(dateString.replace("T", " ").replace("Z", ""));
                consumption.setConsumptionTime(timestamp);
                consumption.setAmount(entry.getValue());
                result.add(consumption);

                logger.info("Summed consumption: Date = {}, Amount = {}", entry.getKey(), entry.getValue());

            } catch (Exception e) {
                logger.error("Error converting date string to Timestamp for date: {}", entry.getKey(), e);
                continue;
            }
        }

        logger.info("Completed summing and converting consumptions. Returning {} result(s).", result.size());
        return result;
    }
}
