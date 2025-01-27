package enefit.rasmushaug.enefitpower.model;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "metering_points")
public class MeteringPoints {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long meteringPointId;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    private String address;

    @OneToMany(mappedBy = "meteringPoint", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Consumption> consumptionRecords;

    // GETTERS
    public String getAddress() {
        return address;
    }

    public Customer getCustomer() {
        return customer;
    }

    public long getMeteringPointId() {
        return meteringPointId;
    }

    public List<Consumption> getConsumptionRecords() {
        return consumptionRecords;
    }

    // SETTERS
    public void setAddress(String address) {
        this.address = address;
    }

    public void setCustomer(Customer customer) {
        this.customer= customer;
    }

    public void setMeteringPointId(long meteringPointId) {
        this.meteringPointId = meteringPointId;
    }

    public void setConsumptionRecords(List<Consumption> consumptionRecords) {
        this.consumptionRecords = consumptionRecords;
    }
}
