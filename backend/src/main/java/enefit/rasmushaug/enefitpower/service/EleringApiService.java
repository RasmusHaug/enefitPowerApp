package enefit.rasmushaug.enefitpower.service;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import enefit.rasmushaug.enefitpower.model.EleringData;
import enefit.rasmushaug.enefitpower.repository.EleringDataRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class EleringApiService {
    private final RestTemplate restTemplate;
    private final EleringDataRepository eleringDataRepository;
    private static final String ELERING_URL = "https://estfeed.elering.ee/api/public/v1/energy-price/electricity";
    private static final Logger logger = LoggerFactory.getLogger(EleringApiService.class);

    public EleringApiService(RestTemplate restTemplate, EleringDataRepository eleringDataRepository) {
        this.restTemplate = restTemplate;
        this.eleringDataRepository = eleringDataRepository;
    }

    @Scheduled(cron = "0 0 3 * * ?")
    public List<EleringData> fetchDataAndStoreToday() {
        LocalDate today = LocalDate.now();
        String startDateTime = today.atStartOfDay().toString() + "Z";
        String endDateTime = startDateTime;

        String url = String.format("%s?startDateTime=%s&endDateTime=%s", ELERING_URL, startDateTime, endDateTime);

        return fetchDataFromEleringApi(url, today);
    }

    public List<EleringData> fetchDataAndStoreDate(LocalDate date) {
        String startDateTime = date.atStartOfDay().toString() + "Z";
        String endDateTime = startDateTime;

        String url = String.format("%s?startDateTime=%s&endDateTime=%s", ELERING_URL, startDateTime, endDateTime);
        return fetchDataFromEleringApi(url, date);
    }

    public List<EleringData> fetchDataFromEleringApi(String url, LocalDate date) {
        List<EleringData> eleringDataList = new ArrayList<>();
        List<EleringData> existingData = eleringDataRepository.findByDate(date);
        if (!existingData.isEmpty()) {
            logger.info("Data for date {} already exists. Skipping fetch.", date);
            return existingData;
        }

        logger.info("Fetching data from ELERING using URL: {}", url);
        EleringData[] eleringModels = restTemplate.getForObject(url, EleringData[].class);

        if (eleringModels == null) {
            return List.of();
        }

        for (EleringData eleringData : eleringModels) {
            logger.info("Adding eleringData: {}", eleringData);
            eleringData.setDate(date);
            eleringDataList.add(eleringData);
        }

        eleringDataRepository.saveAll(eleringDataList);
        return eleringDataList;
    }
}