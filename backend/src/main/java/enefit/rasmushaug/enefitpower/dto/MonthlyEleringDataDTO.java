package enefit.rasmushaug.enefitpower.dto;

public class MonthlyEleringDataDTO {
    private String date;
    private double centsPerKwh;
    private double centsPerKwhWithVat;
    private double eurPerMwh;
    private double eurPerMwhWithVat;

    public MonthlyEleringDataDTO(String date, double centsPerKwh, double centsPerKwhWithVat, double eurPerMwh, double eurPerMwhWithVat) {
        this.date = date;
        this.centsPerKwh = centsPerKwh;
        this.centsPerKwhWithVat = centsPerKwhWithVat;
        this.eurPerMwh = eurPerMwh;
        this.eurPerMwhWithVat = eurPerMwhWithVat;
    }

    public String getdate() {
        return date;
    }

    public double getcentsPerKwh() {
        return centsPerKwh;
    }

    public double getCentsPerKwhWithVat() {
        return centsPerKwhWithVat;
    }

    public double getEurPerMwh() {
        return eurPerMwh;
    }

    public double getEurPerMwhWithVat() {
        return eurPerMwhWithVat;
    }
}