package enefit.rasmushaug.enefitpower.controller;

import org.springframework.web.bind.annotation.RestController;

import enefit.rasmushaug.enefitpower.model.EleringData;
import enefit.rasmushaug.enefitpower.service.EleringApiService;

import java.time.LocalDate;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/api")
public class EleringApiController {
    private final EleringApiService eleringApiService;

    public EleringApiController(EleringApiService eleringApiService) {
        this.eleringApiService = eleringApiService;
    }

    @GetMapping("/fetch-elering-today")
    public ResponseEntity<List<EleringData>> fetchEleringToday() {
        return ResponseEntity.ok(eleringApiService.fetchDataAndStoreToday());
    }

    @GetMapping("/fetch-elering-date")
    public ResponseEntity<List<EleringData>> fetchEleringDate(@RequestParam LocalDate date) {
        return ResponseEntity.ok(eleringApiService.fetchDataAndStoreDate(date));
    }
}
