package enefit.rasmushaug.enefitpower.dto;

import enefit.rasmushaug.enefitpower.model.Customer;


/**
 * DTO for representing customer data in the response
 * This class is used to transfer customer information in a
 * simplified format to be sent in API responses.
 *
 * This class does not include sensitive information such as the password for security reasons.
 */
public class CustomerResponse {
    private Long customerId;
    private String firstName;
    private String lastName;
    private String username;

    /**
     * Constructs a new CustomerResponse object from the provided Customer entity.
     * This constructor maps the relevant fields from Customer entity to the
     * CustomerResponse DTO for use in API responses.
     *
     * @param customer The customer entity to extract data from.
     */
    public CustomerResponse(Customer customer) {
        this.customerId = customer.getCustomerId();
        this.firstName = customer.getFirstName();
        this.lastName = customer.getLastName();
        this.username = customer.getUsername();
    }

    /**
     * Getter method to obtain the unique identifier of the customer.
     *
     * @return Long The unique identifier (ID) of the customer.
     */
    public Long getCustomerId() {
        return customerId;
    }

    /**
     * Getter method to obtain customer first name.
     *
     * @return String The first name of the customer.
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Getter method to obtain customer last name.
     *
     * @return String The last name of the customer.
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Getter method to obtain customer username.
     *
     * @return String The username of the customer.
     */
    public String getUsername() {
        return username;
    }
}
