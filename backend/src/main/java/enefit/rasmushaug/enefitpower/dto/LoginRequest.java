package enefit.rasmushaug.enefitpower.dto;

/**
 * Represents a login request containing the user's credentials.
 *
 * This class is used to encapsulate the data sent in the login request, consisting of
 * the customer's username and password as that's all that is required.
 * It is used by the login API endpoint to validate a user's
 * credentials during the authentication process.
 *
 * This class should be used to avoid using multiple @RequestBody annotations and solves the problem by passing a single object in the request body.
 */
public class LoginRequest {
    private String username;
    private String password;


    /**
     * Getter to get the password provided in the login request.
     *
     * @return String The password for the login request
     */
    public String getPassword() {
        return password;
    }

    /**
     * Getter to get the username provided in the login request.
     *
     * @return String The username for the login request.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Setter to set the password for login request so that Spring can properly
     * read it and compare it to one stored in database.
     *
     * @param password The password to be set for the login request.
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Setter to set the username for login request.
     * Solves the same issue as the setter above.
     *
     * @param username The username to be set for the login request.
     */
    public void setUsername(String username) {
        this.username = username;
    }
}
