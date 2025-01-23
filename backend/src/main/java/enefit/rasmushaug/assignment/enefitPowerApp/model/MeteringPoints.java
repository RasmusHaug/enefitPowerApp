package enefit.rasmushaug.assignment.enefitPowerApp.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "metering_points")
public class MeteringPoints {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long meteringPointId;

    private long customerId;

    private String address;

    // GETTERS
    public String getAddress() {
        return address;
    }

    public long getCustomerId() {
        return customerId;
    }

    public long getMeteringPointId() {
        return meteringPointId;
    }

    // SETTERS
    public void setAddress(String address) {
        this.address = address;
    }

    public void setCustomerId(long customerId) {
        this.customerId = customerId;
    }

    public void setMeteringPointId(long meteringPointId) {
        this.meteringPointId = meteringPointId;
    }
}
