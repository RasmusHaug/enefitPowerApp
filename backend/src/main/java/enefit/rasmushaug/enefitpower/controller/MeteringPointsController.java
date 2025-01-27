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

    @PostMapping("/{customerId}/add-metering-points")
    public ResponseEntity<?> addMeteringPoint(@PathVariable Long customerId, @RequestBody MeteringPoints meteringPoints, @RequestHeader("Authorization") String token) {
        String loggedInUsername = jwtUtil.extractUsername(token.replace("Bearer ", ""));
        Customer loggedInCustomer = customerService.getCustomerByUsername(loggedInUsername);
        logger.debug("{} {}",loggedInCustomer.getCustomerId(), customerId);
        if (!loggedInCustomer.getCustomerId().equals(customerId)) {
            logger.error("Unauthorised user '{}' tried to create metering point for '{}'", loggedInUsername, customerId);
            return ResponseEntity.status(403).body("You are not authorized to add metering points.");
        }

        meteringPoints.setCustomer(loggedInCustomer);
        MeteringPoints savedMeteringPoint = meteringPointsService.saveMeteringPoint(meteringPoints);
        logger.info("Created new metering point for user '{}'", loggedInUsername);
        return ResponseEntity.ok(savedMeteringPoint);
    }

    @GetMapping("/{customerId}/get-metering-points")
    public ResponseEntity<?> getMeteringPoints(@PathVariable Long customerId, @RequestHeader("Authorization") String token) {
        String loggedInUsername = jwtUtil.extractUsername(token.replace("Bearer ", ""));
        Customer loggedInCustomer = customerService.getCustomerByUsername(loggedInUsername);

        if (!loggedInCustomer.getCustomerId().equals(customerId)) {
            logger.error("Unauthorised user '{}' tried to get metering points for user '{}'", loggedInCustomer, customerId);
            return ResponseEntity.status(403).body("You are not authorized to view metering points.");
        }
        List<MeteringPoints> meteringPoints = meteringPointsService.getMeteringPointsByCustomerId(customerId);
        logger.info("User '{}' read metering points associated with customer '{}'", loggedInUsername, customerId);
        return ResponseEntity.ok(meteringPoints);
    }
}
