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
    private String sessionId;

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
     * Returns a string representation of the customer object.
     *
     * This method provides a simplified version of the customer details, excluding the password.
     * Primarily meant to be used for logging and debugging purposes.
     *
     * @return A string representing the customer object, including customerId, firstName, lastName, and username.
     */
    @Override
    public String toString() {
        return "Customer{" +
            "customerId=" + customerId +
            ", firstName='" + firstName + '\'' +
            ", lastName='" + lastName + '\'' +
            ", username='" + username + '\'' +
            ", sessionId='" + sessionId + '\'' +
            '}';
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

    /**
     * Getter method to obtain customer sessionId.
     *
     * @return String The sessionId of the customer.
     */
    public String getSessionId() {
        return sessionId;
    }

    /**
     * Setter method to set customer sessionId.
     *
     */
    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }
}
