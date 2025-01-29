package enefit.rasmushaug.enefitpower.dto;

import java.time.LocalDate;

import enefit.rasmushaug.enefitpower.model.Consumption.AmountUnit;

public class ConsumptionDTO {

    private Long consumptionId;
    private Double amount;
    private AmountUnit amountUnit;
    private LocalDate consumptionTime;
    private String date;

    public ConsumptionDTO(Long consumptionId, Double amount, AmountUnit amountUnit, LocalDate consumptionTime) {
        this.consumptionId = consumptionId;
        this.amount = amount;
        this.amountUnit = amountUnit;
        this.consumptionTime = consumptionTime;
    }

    // Constructor for getting yearly data
    public ConsumptionDTO(Double amount, String date) {
        this.amount = amount;
        this.date = date;
    }

    public Long getConsumptionId() {
        return consumptionId;
    }
    public Double getAmount() {
        return amount;
    }
    public AmountUnit getAmountUnit() {
        return amountUnit;
    }
    public LocalDate getConsumptionTime() {
        return consumptionTime;
    }
    public String getDate() {
        return date;
    }

    public void setConsumptionId(Long consumptionId) {
        this.consumptionId = consumptionId;
    }
    public void setAmount(Double amount) {
        this.amount = amount;
    }
    public void setAmountUnit(AmountUnit amountUnit) {
        this.amountUnit = amountUnit;
    }
    public void setConsumptionTime(LocalDate consumptionTime) {
        this.consumptionTime = consumptionTime;
    }
    public void setDate(String date) {
        this.date = date;
    }
}
