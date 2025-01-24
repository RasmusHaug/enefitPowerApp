package enefit.rasmushaug.enefitpower.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import enefit.rasmushaug.enefitpower.model.EleringData;

import java.util.List;

@Service
public class EleringApiService {
    private final RestTemplate restTemplate;

    public EleringApiService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<EleringData> fetchDataFromEleringApi(String url) {
        EleringData[] eleringModels = restTemplate.getForObject(url, EleringData[].class);
        return List.of(eleringModels);
    }
}