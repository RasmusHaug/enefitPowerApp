package enefit.rasmushaug.enefitpower.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import enefit.rasmushaug.enefitpower.dto.ConsumptionDTO;
import enefit.rasmushaug.enefitpower.model.Consumption;
import enefit.rasmushaug.enefitpower.model.Consumption.AmountUnit;
import enefit.rasmushaug.enefitpower.model.MeteringPoints;
import enefit.rasmushaug.enefitpower.repository.ConsumptionRepository;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Service layer for managing consumption operations.
 *
 * This service provides methods to save consumption for a metering point, finding customer electricity
 * consumption using customer ID and finding consumption for a specific metering point.
 */
@Service
public class ConsumptionService {
    private static final Logger logger = LoggerFactory.getLogger(ConsumptionService.class);

    @Autowired
    private ConsumptionRepository consumptionRepository;

    @Autowired
    private MeteringPointsService meteringPointsService;

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

    public List<ConsumptionDTO> getCustomerYearlyConsumption(Long customerId) {
        List<MeteringPoints> meteringPoints = meteringPointsService.getMeteringPointsByCustomerId(customerId);

        if (meteringPoints.isEmpty()) {
            return Collections.emptyList();
        }

        LocalDate startDate = LocalDate.now().minusYears(1);
        LocalDate endDate = LocalDate.now();

        return calculateMonthlyConsumptions(startDate, endDate, meteringPoints);
    }

    private List<ConsumptionDTO> calculateMonthlyConsumptions(LocalDate startDate, LocalDate endDate, List<MeteringPoints> meteringPoints) {
        List<ConsumptionDTO> allConsumptions = new ArrayList<>();

        for (MeteringPoints meteringPoint : meteringPoints) {
            allConsumptions.addAll(sumDailyConsumptions(meteringPoint.getConsumptionRecords()));
        }

        return allConsumptions.stream()
                .filter(consumption -> !consumption.getConsumptionTime().isBefore(startDate) && !consumption.getConsumptionTime().isAfter(endDate))
                .collect(Collectors.groupingBy(
                        consumption -> YearMonth.from(consumption.getConsumptionTime()),
                        Collectors.reducing(0.0, ConsumptionDTO::getAmount, Double::sum)
                ))
                .entrySet().stream()
                .map(entry -> new ConsumptionDTO(
                        entry.getValue(),
                        entry.getKey().toString()
                ))
                .toList();
    }

        public List<ConsumptionDTO> sumDailyConsumptions(List<Consumption> consumptions) {
        Map<LocalDate, Double> summedConsumptions = new HashMap<>();
        Map<LocalDate, Long> firstConsumptionIds = new HashMap<>();
        Map<LocalDate, AmountUnit> firstAmountUnits = new HashMap<>();

        for (Consumption consumption : consumptions) {
            LocalDate consumptionDate = consumption.getConsumptionTime();

            summedConsumptions.merge(consumptionDate, consumption.getAmount(), Double::sum);

            firstConsumptionIds.putIfAbsent(consumptionDate, consumption.getConsumptionId());
            firstAmountUnits.putIfAbsent(consumptionDate, consumption.getAmountUnit());
        }

        List<ConsumptionDTO> consumptionDTOs = new ArrayList<>();
        for (Map.Entry<LocalDate, Double> entry : summedConsumptions.entrySet()) {
            LocalDate date = entry.getKey();
            Double summedAmount = entry.getValue();

            Long consumptionId = firstConsumptionIds.get(date);
            AmountUnit amountUnit = firstAmountUnits.get(date);

            ConsumptionDTO consumptionDTO = new ConsumptionDTO(
                    consumptionId,
                    summedAmount,
                    amountUnit,
                    date
            );
            consumptionDTOs.add(consumptionDTO);
        }

        return consumptionDTOs;
    }
}
