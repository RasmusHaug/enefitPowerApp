package enefit.rasmushaug.enefitpower.model;

import java.sql.Timestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "consumption")
public class Consumption {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long consumptionId;

    @ManyToOne
    @JoinColumn(name = "metering_point_id", nullable = false)
    private MeteringPoints meteringPoint;

    private Double amount;
    private String amountUnit;

    @Column(name = "consumption_time")
    private Timestamp consumptionTime;

    // GETTERS
    public Double getAmount() {
        return amount;
    }

    public String getAmountUnit() {
        return amountUnit;
    }

    public long getConsumptionId() {
        return consumptionId;
    }

    public Timestamp getConsumptionTime() {
        return consumptionTime;
    }

    public MeteringPoints getMeteringPoint() {
        return meteringPoint;
    }

    // SETTERS
    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public void setAmountUnit(String amountUnit) {
        this.amountUnit = amountUnit;
    }

    public void setConsumptionId(long consumptionId) {
        this.consumptionId = consumptionId;
    }

    public void setConsumptionTime(Timestamp consumptionTime) {
        this.consumptionTime = consumptionTime;
    }

    public void setMeteringPoint(MeteringPoints meteringPoint) {
        this.meteringPoint= meteringPoint;
    }
}
