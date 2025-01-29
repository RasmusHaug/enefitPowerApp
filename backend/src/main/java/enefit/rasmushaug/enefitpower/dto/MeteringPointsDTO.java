package enefit.rasmushaug.enefitpower.dto;

import java.util.List;

public class MeteringPointsDTO {
    private long meteringPointId;
    private String name;
    private String address;
    private String city;
    private String postalCode;
    private List<ConsumptionDTO> consumptions;

    public MeteringPointsDTO(long meteringPointId, String name, String address, String city, String postalCode, List<ConsumptionDTO> consumtions) {
        this.meteringPointId = meteringPointId;
        this.name = name;
        this.address = address;
        this.city = city;
        this.postalCode = postalCode;
        this.consumptions = consumtions;
    }

    public long getMeteringPointId() {
        return meteringPointId;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getCity() {
        return city;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public List<ConsumptionDTO> getConsumptions() {
        return consumptions;
    }

    public void setMeteringPointId(long meteringPointId) {
        this.meteringPointId = meteringPointId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public void setConsumptions(List<ConsumptionDTO> consumptions) {
        this.consumptions = consumptions;
    }
}
