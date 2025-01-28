package enefit.rasmushaug.enefitpower.model;

import java.time.LocalDate;
import java.time.OffsetDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "elering_data")
public class EleringData {

    @Id
    private LocalDate date;

    @JsonProperty("centsPerKwh")
    private double centsPerKwh;

    @JsonProperty("centsPerKwhWithVat")
    private double centsPerKwhVat;

    @JsonProperty("eurPerMwh")
    private double eurPerMwh;

    @JsonProperty("eurPerMwhWithVat")
    private double eurPerMwhVat;

    @JsonProperty("fromDateTime")
    private OffsetDateTime fromDateTime;

    @JsonProperty("toDateTime")
    private OffsetDateTime toDateTime;

    public EleringData() {}

    public EleringData(double centsPerKwh, double centsPerKwhVat, double eurPerMwh, double eurPerMwhVat, OffsetDateTime fromDateTime, OffsetDateTime toDateTime) {
        this.centsPerKwh = centsPerKwh;
        this.centsPerKwhVat = centsPerKwhVat;
        this.eurPerMwh = eurPerMwh;
        this.eurPerMwhVat = eurPerMwhVat;
        this.fromDateTime = fromDateTime;
        this.toDateTime = toDateTime;
        this.date = fromDateTime.toLocalDate();
    }

    @Override
    public String toString() {
        return "eleringData{" +
                "centsPerKwh=" + centsPerKwh +
                ", centsPerKwhVat=" + centsPerKwhVat +
                ", eurPerMwh=" + eurPerMwh +
                ", eurPerMwhVat=" + eurPerMwhVat +
                ", fromDateTime=" + fromDateTime +
                ", toDateTime=" + toDateTime +
                '}';
    }

    // GETTERS
    public double getCentsPerKwh() {
        return centsPerKwh;
    }
    public double getCentsPerKwhVat() {
        return centsPerKwhVat;
    }
    public double getEurPerMwh() {
        return eurPerMwh;
    }
    public double getEurPerMwhVat() {
        return eurPerMwhVat;
    }
    public OffsetDateTime getFromDateTime() {
        return fromDateTime;
    }
    public OffsetDateTime getToDateTime() {
        return toDateTime;
    }
    public LocalDate getDate() {
        return date;
    }

    // SETTERS
    public void setCentsPerKwh(double centsPerKwh) {
        this.centsPerKwh = centsPerKwh;
    }
    public void setCentsPerKwhVat(double centsPerKwhVat) {
        this.centsPerKwhVat = centsPerKwhVat;
    }
    public void setEurPerMwh(double eurPerMwh) {
        this.eurPerMwh = eurPerMwh;
    }
    public void setEurPerMwhVat(double eurPerMwhVat) {
        this.eurPerMwhVat = eurPerMwhVat;
    }
    public void setFromDateTime(OffsetDateTime fromDateTime) {
        this.fromDateTime = fromDateTime;
    }
    public void setToDateTime(OffsetDateTime toDateTime) {
        this.toDateTime = toDateTime;
    }
    public void setDate(LocalDate date) {
        this.date = date;
    }
}
