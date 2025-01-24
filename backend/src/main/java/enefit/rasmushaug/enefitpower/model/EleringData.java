package enefit.rasmushaug.enefitpower.model;

import java.time.OffsetDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

public class EleringData {
    @JsonProperty("centsPerKwh")
    private double centsPerKwh;

    @JsonProperty("centsPerKwhVat")
    private double centsPerKwhVat;

    @JsonProperty("eurPerMwh")
    private double eurPerMwh;

    @JsonProperty("eurPerMwhVat")
    private double eurPerMwhVat;

    @JsonProperty("fromDateTime")
    private OffsetDateTime fromDateTime;

    @JsonProperty("toDateTime")
    private OffsetDateTime toDateTime;

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
}
