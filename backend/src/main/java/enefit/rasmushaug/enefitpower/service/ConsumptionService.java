package enefit.rasmushaug.enefitpower.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import enefit.rasmushaug.enefitpower.model.Consumption;
import enefit.rasmushaug.enefitpower.repository.ConsumptionRepository;

import java.util.List;

/**
 * Service layer for managing consumption operations.
 *
 * This service provides methods to save consumption for a metering point, finding customer electricity
 * consumption using customer ID and finding consumption for a specific metering point.
 */
@Service
public class ConsumptionService {

    @Autowired
    private ConsumptionRepository consumptionRepository;

    /**
     * Saves Customer consumption for a specific metering point to the database.
     *
     * @param consumption The consumption object storing references what metering point
     *                    and to what customer the electricity was consumed.
     * @return The Consumption object.
     */
    public Consumption saveConsumption(Consumption consumption) {
        return consumptionRepository.save(consumption);
    }

    /**
     * Gets All consumptions for a specific Customer.
     *
     * @param customerId The Customer ID value to obtain the data.
     * @return List of Consumptions related to that customer.
     */
    public List<Consumption> getConsumptionsByCustomerId(Long customerId) {
        return consumptionRepository.findByMeteringPointCustomerCustomerId(customerId);
    }

    /**
     * Gets all consumptions for a specific metering point.
     *
     * @param meteringPointId The Metering Point ID value to obtain the data for.
     * @return List of Consumptions related to that Metering Point.
     */
    public List<Consumption> getConsumptionsByMeteringPointId(Long meteringPointId) {
        return consumptionRepository.findByMeteringPointMeteringPointId(meteringPointId);
    }
}
