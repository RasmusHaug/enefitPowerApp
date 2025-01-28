package enefit.rasmushaug.enefitpower.controller;

import org.springframework.web.bind.annotation.RestController;

import enefit.rasmushaug.enefitpower.dto.MonthlyEleringData;
import enefit.rasmushaug.enefitpower.service.EleringApiService;

import java.time.LocalDate;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@RestController
@RequestMapping("/api")
public class EleringApiController {
    private final EleringApiService eleringApiService;

    public EleringApiController(EleringApiService eleringApiService) {
        this.eleringApiService = eleringApiService;
    }

    @GetMapping("/fetch-elering-date-year")
    public ResponseEntity<List<MonthlyEleringData>> fetchEleringDataYear() {
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusYears(1);
        return ResponseEntity.ok(eleringApiService.fetchMonthlyEleringData(startDate, endDate));
    }
}
