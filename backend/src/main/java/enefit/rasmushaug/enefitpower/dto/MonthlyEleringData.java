package enefit.rasmushaug.enefitpower.dto;

public class MonthlyEleringData {
    private String yearMonth;
    private double averageCentsPerKwh;
    private double centsPerKwhWithVat;
    private double eurPerMwh;
    private double eurPerMwhWithVat;

    public MonthlyEleringData(String yearMonth, double averageCentsPerKwh, double centsPerKwhWithVat, double eurPerMwh, double eurPerMwhWithVat) {
        this.yearMonth = yearMonth;
        this.averageCentsPerKwh = averageCentsPerKwh;
        this.centsPerKwhWithVat = centsPerKwhWithVat;
        this.eurPerMwh = eurPerMwh;
        this.eurPerMwhWithVat = eurPerMwhWithVat;
    }

    public String getYearMonth() {
        return yearMonth;
    }

    public double getAverageCentsPerKwh() {
        return averageCentsPerKwh;
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