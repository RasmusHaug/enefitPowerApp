package enefit.rasmushaug.enefitpower.controller;

import org.springframework.web.bind.annotation.RestController;

import enefit.rasmushaug.enefitpower.model.EleringData;
import enefit.rasmushaug.enefitpower.service.EleringApiService;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/api")
public class EleringApiController {
    private final EleringApiService eleringApiService;
    private static final String ELERING_URL = "https://estfeed.elering.ee/api/public/v1/energy-price/electricity";

    public EleringApiController(EleringApiService eleringApiService) {
        this.eleringApiService = eleringApiService;
    }

    @GetMapping("/fetch-elering-data")
    public List<EleringData> fetchEleringData(
            @RequestParam String startDateTime,
            @RequestParam String endDateTime
            ) {
        String url = String.format("%s?startDateTime=%s&endDateTime=%s", ELERING_URL, startDateTime, endDateTime);
        return eleringApiService.fetchDataFromEleringApi(url);
    }
}
