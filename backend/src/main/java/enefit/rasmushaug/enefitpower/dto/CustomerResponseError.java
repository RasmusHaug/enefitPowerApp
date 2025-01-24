package enefit.rasmushaug.enefitpower.dto;

/**
 * Represents an error response for a customer-related operation.
 *
 * This class extends {@link CustomerResponse} and is used to convey error messages when
 * an operation involving a customer (such as registration or login) fails. It includes a message
 * that describes the error.
 */
public class CustomerResponseError extends CustomerResponse {

    private String message;

    /**
     * Constructs a new {@link CustomerResponseError} with the specified error message.
     *
     * This constructor calls the superclass constructor with a `null` argument and sets the
     * message to the provided value.
     *
     * @param message The error message describing the issue.
     */
    public CustomerResponseError(String message) {
        super(null);  // Calls the superclass constructor with null value
        this.message = message;
    }

    /**
     * Retrieves the error message associated with the response.
     *
     * @return The error message as a String.
     */
    public String getMessage() {
        return message;
    }

    /**
     * Sets the error message for this response.
     *
     * @param message The error message to set.
     */
    public void setMessage(String message) {
        this.message = message;
    }
}