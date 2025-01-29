package enefit.rasmushaug.enefitpower.model;

import java.time.LocalDate;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
    @JsonBackReference
    private MeteringPoints meteringPoint;

    private Double amount;

    @Enumerated(EnumType.STRING)
    private String amountUnit;

    @Column(name = "consumption_time")
    private LocalDate consumptionTime;

    public Double getAmount() {
        return amount;
    }
    public String getAmountUnit() {
        return amountUnit;
    }
    public long getConsumptionId() {
        return consumptionId;
    }
    public LocalDate getConsumptionTime() {
        return consumptionTime;
    }
    public MeteringPoints getMeteringPoint() {
        return meteringPoint;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }
    public void setAmountUnit(String amountUnit) {
        this.amountUnit = amountUnit;
    }
    public void setConsumptionId(long consumptionId) {
        this.consumptionId = consumptionId;
    }
    public void setConsumptionTime(LocalDate consumptionTime) {
        this.consumptionTime = consumptionTime;
    }
    public void setMeteringPoint(MeteringPoints meteringPoint) {
        this.meteringPoint= meteringPoint;
    }

    public enum AmountUnit {
        kWh, mWh
    }

    @Override
    public String toString() {
        return "Consumption{" +
            "consumptionId=" + consumptionId +
            ", meteringPoint=" + (meteringPoint != null ? meteringPoint.getMeteringPointId() : "null") +
            ", amount=" + amount +
            ", amountUnit='" + amountUnit + '\'' +
            ", consumptionTime=" + consumptionTime +
            '}';
    }
}
