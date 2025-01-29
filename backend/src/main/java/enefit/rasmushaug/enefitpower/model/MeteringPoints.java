package enefit.rasmushaug.enefitpower.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

/**
 * Represents a metering point entity associated with a customer.
 * This entity contains details about a specific metering point, including its name, address, and associated consumption records.
 * Each metering point is linked to a customer and can have multiple consumption records.
 */
@Entity
@Table(name = "metering_points")
public class MeteringPoints {
    /**
     * Unique identifier for the metering point.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long meteringPointId;

    /**
     * The customer associated with this metering point.
     * Uses a many-to-one relationship since a customer can have multiple metering points.
     */
    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    @JsonBackReference
    private Customer customer;

    private String name;
    private String address;
    private String city;
    private String postalCode;

    @OneToMany(mappedBy = "meteringPoint", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Consumption> consumptionRecords;

    public MeteringPoints() {}

    public MeteringPoints(String name, String address, String city, String postalCode, Customer customer) {
        this.name = name;
        this.address = address;
        this.city = city;
        this.postalCode = postalCode;
        this.customer = customer;
    }

    // GETTERS
    /**
     * Gets the address of the metering point.
     *
     * @return Address of the metering point.
     */
    public String getAddress() {
        return address;
    }

    /**
     * Gets the city of the metering point.
     *
     * @return City of the metering point.
     */
    public String getCity() {
        return city;
    }

    /**
     * Gets the name of the metering point.
     *
     * @return Name of the metering point.
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the postal code of the metering point.
     *
     * @return Postal code of the metering point.
     */
    public String getPostalCode() {
        return postalCode;
    }

    /**
     * Gets the customer associated with this metering point.
     *
     * @return The associated customer.
     */
    public Customer getCustomer() {
        return customer;
    }

    /**
     * Gets the unique identifier for this metering point.
     *
     * @return The metering point ID.
     */
    public long getMeteringPointId() {
        return meteringPointId;
    }

    /**
     * Gets the list of consumption records for this metering point.
     *
     * @return List of associated consumption records.
     */
    public List<Consumption> getConsumptionRecords() {
        return consumptionRecords;
    }

    // SETTERS
    /**
     * Sets the address of the metering point.
     *
     * @param address Address to set for the metering point.
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * Sets the city of the metering point.
     *
     * @param city City to set for the metering point.
     */
    public void setCity(String city) {
        this.city = city;
    }

    /**
     * Sets the name of the metering point.
     *
     * @param name Name to set for the metering point.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Sets the postal code of the metering point.
     *
     * @param postalCode Postal code to set for the metering point.
     */
    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    /**
     * Sets the customer for this metering point.
     *
     * @param customer Customer to associate with this metering point.
     */
    public void setCustomer(Customer customer) {
        this.customer= customer;
    }

    /**
     * Sets the unique identifier for this metering point.
     *
     * @param meteringPointId The ID to set for the metering point.
     */
    public void setMeteringPointId(long meteringPointId) {
        this.meteringPointId = meteringPointId;
    }

    /**
     * Sets the list of consumption records for this metering point.
     *
     * @param consumptionRecords List of consumption records to associate.
     */
    public void setConsumptionRecords(List<Consumption> consumptionRecords) {
        this.consumptionRecords = consumptionRecords;
    }

    @Override
    public String toString() {
        return "MeteringPoints{" +
                "meteringPointId=" + meteringPointId +
                ", customerId=" + (customer != null ? customer.getCustomerId() : "null") +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", city='" + city + '\'' +
                ", postalCode='" + postalCode + '\'' +
                ", consumptionRecordsCount=" + (consumptionRecords != null ? consumptionRecords.size() : 0) +
                '}';
    }
}
