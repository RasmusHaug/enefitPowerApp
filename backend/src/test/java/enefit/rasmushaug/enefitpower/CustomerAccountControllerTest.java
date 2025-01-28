package enefit.rasmushaug.enefitpower;

import com.fasterxml.jackson.databind.ObjectMapper;

import enefit.rasmushaug.enefitpower.model.Customer;
import enefit.rasmushaug.enefitpower.repository.CustomerRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class CustomerAccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CustomerRepository customerRepository;

    private ObjectMapper objectMapper;

    /**
     * Initializes test dependencies and cleans up the database before each test.
     * This ensures no leftover data affects the tests.
     */
    @BeforeEach
    public void setUp() {
        objectMapper = new ObjectMapper();
        customerRepository.deleteAll(); // Clear all existing customer data
    }

    /**
     * Test case for successful customer registration.
     *
     * This test simulates registering a new customer and expects a successful
     * response with HTTP status 200. It also validates that the customer
     * information is correctly saved in the database.
     *
     * @throws Exception if there are any errors during the test execution
     */
    @Test
    void testRegisterCustomer_Success() throws Exception {
        Customer customer = new Customer();
        customer.setFirstName("Test");
        customer.setLastName("User");
        customer.setUsername("testUser");
        customer.setPassword("password123");

        mockMvc.perform(post("/api/customers/register")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(customer)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Test"))
                .andExpect(jsonPath("$.lastName").value("User"))
                .andExpect(jsonPath("$.username").value("testUser"))
                .andExpect(jsonPath("$.sessionId").doesNotExist());

        // Verify the customer is saved in the database
        Customer savedCustomer = customerRepository.findByUsername("testUser");
        assert savedCustomer != null;
        assert savedCustomer.getFirstName().equals("Test");
        assert savedCustomer.getLastName().equals("User");
        assert savedCustomer.getUsername().equals("testUser");
    }

    /**
     * Test case for registering a customer with a username that is already taken.
     *
     * This test simulates attempting to register a new customer with a username
     * that already exists in the database. It expects an HTTP status 500
     * and an error message indicating that the username is already taken.
     *
     * @throws Exception if there are any errors during the test execution
     */
    @Test
    void testRegisterCustomer_UsernameAlreadyTaken() throws Exception {
        // Create and save an existing customer with a username
        Customer existingCustomer = new Customer();
        existingCustomer.setFirstName("Existing");
        existingCustomer.setLastName("User");
        existingCustomer.setUsername("takenUsername");
        existingCustomer.setPassword("password123");
        customerRepository.save(existingCustomer);

        // Create a new customer with the same username
        Customer newCustomer = new Customer();
        newCustomer.setFirstName("New");
        newCustomer.setLastName("User");
        newCustomer.setUsername("takenUsername");
        newCustomer.setPassword("password123");

        mockMvc.perform(post("/api/customers/register")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(newCustomer)))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Username already taken"));
    }

    /**
     * Test case for successful customer login.
     * It tests the scenario where valid credentials are provided,
     * and expects an HTTP 200 status with customer details and a JWT token in the response.
     */
    @Test
    void testLogin_Success() throws Exception {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        String encodedPassword = passwordEncoder.encode("password123");

        Customer customer = new Customer();
        customer.setFirstName("John");
        customer.setLastName("Doe");
        customer.setUsername("johnDoe123");
        customer.setPassword(encodedPassword);
        customerRepository.save(customer);

        mockMvc.perform(post("/api/customers/login")
                .contentType("application/json")
                .content("{ \"username\": \"johnDoe123\", \"password\": \"password123\" }"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("johnDoe123"))
                .andExpect(jsonPath("$.sessionId").exists());
    }

    /**
     * Test case for login failure with invalid credentials.
     * It tests the scenario where invalid credentials are provided,
     * and expects an HTTP 401 status with the error message "Login failed."
     */
    @Test
    void testLogin_Failure_InvalidCredentials() throws Exception {
        mockMvc.perform(post("/api/customers/login")
                .contentType("application/json")
                .content("{ \"username\": \"nonexistentUser\", \"password\": \"wrongPassword\" }"))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Login failed."));
    }
}
