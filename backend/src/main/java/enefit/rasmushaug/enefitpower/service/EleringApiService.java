package enefit.rasmushaug.enefitpower.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import enefit.rasmushaug.enefitpower.dto.MonthlyEleringData;
import enefit.rasmushaug.enefitpower.model.EleringData;
import enefit.rasmushaug.enefitpower.repository.EleringDataRepository;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EleringApiService {
    private final RestTemplate restTemplate;
    private final EleringDataRepository eleringDataRepository;
    private static final String ELERING_URL = "https://estfeed.elering.ee/api/public/v1/energy-price/electricity";
    private static final Logger logger = LoggerFactory.getLogger(EleringApiService.class);

    // To save time using final variable for average VAT price.
    private static final double VAT_RATE = 0.22;

    public EleringApiService(RestTemplate restTemplate, EleringDataRepository eleringDataRepository) {
        this.restTemplate = restTemplate;
        this.eleringDataRepository = eleringDataRepository;
    }

    public List<MonthlyEleringData> fetchMonthlyEleringData(LocalDate startDate, LocalDate endDate) {
        populateMissingDailyData(startDate, endDate);
        return calculateMonthlyAverages(startDate, endDate);
    }

    private void populateMissingDailyData(LocalDate startDate, LocalDate endDate) {
        for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
            List<EleringData> excistsData = eleringDataRepository.findByDate(date);
            if (excistsData.isEmpty()) {
                String url = String.format("%s?startDateTime=%sZ&endDateTime=%sZ", ELERING_URL, date.atStartOfDay().toString(), date.atStartOfDay().toString());
                EleringData[] fetchedData = fetchDataFromEleringApi(url);
                for (EleringData data : fetchedData) {
                    data.setDate(date);
                }
                eleringDataRepository.saveAll(Arrays.asList(fetchedData));
            }
        }
    }

    private EleringData[] fetchDataFromEleringApi(String url) {
        try {
            logger.info("Fetching data from ELERING using URL: {}", url);
            EleringData[] eleringModels = restTemplate.getForObject(url, EleringData[].class);

            if (eleringModels == null) {
                logger.error("Failed to obtain data using url '{}'", url);
                return new EleringData[0];
            }
            return eleringModels;
        } catch (HttpClientErrorException.BadRequest e) {
            logger.error("Bad request error while fetching data using URL '{}'", url);
            logger.error("Error response: '{}'", e.getResponseBodyAsString());
            return new EleringData[0];
        } catch (Exception e) {
            logger.error("An unexpected error occurred while fetching data from URL: {}", url, e);
            return new EleringData[0];
        }
    }

    private List<MonthlyEleringData> calculateMonthlyAverages(LocalDate startDate, LocalDate endDate) {
        List<EleringData> dailyData = eleringDataRepository.findByDateBetween(startDate, endDate);
        return dailyData.stream()
            .collect(Collectors.groupingBy(
                data -> YearMonth.from(data.getFromDateTime()),
                Collectors.averagingDouble(EleringData::getCentsPerKwh)
            ))
            .entrySet().stream()
            .map(entry -> {
                    YearMonth date = entry.getKey();
                    double centsPerKwH = entry.getValue();

                    double centsPerKwhWithVat = centsPerKwH * (1 + VAT_RATE);
                    double eurPerMwh = (centsPerKwH / 100) * 10;
                    double eurPerMwhWithVat = (centsPerKwhWithVat / 100) * 10;

                    return new MonthlyEleringData(
                        date.toString(),
                        centsPerKwH,
                        centsPerKwhWithVat,
                        eurPerMwh,
                        eurPerMwhWithVat
                    );
            })
            .toList();
    }
}