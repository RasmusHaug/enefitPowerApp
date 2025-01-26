package enefit.rasmushaug.enefitpower.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.SecurityFilterChain;

import enefit.rasmushaug.enefitpower.model.Customer;
import enefit.rasmushaug.enefitpower.repository.CustomerRepository;

/**
 * Configuration class for setting up security settings in the application.
 *
 * This class is responsible for configuring HTTP security, user authentication, and password encoding.
 * It disables CSRF protection for registering customers and loging customers in.
 * This should be disabled for those operations as otherwise no one can register or log in.
 *
 */
@Configuration
public class SecurityConfig {

    /**
     * Configures HTTP security to define how requests are authorized and authenticated.
     *
     * This configuration disables CSRF protection and specifies that the registration and login
     * endpoints should be accessible without authentication, while all other API endpoints require authentication.
     *
     * @param http The HttpSecurity object used to configure the security settings.
     * @return A SecurityFilterChain that applies the defined security settings.
     * @throws Exception If an error occurs while configuring HTTP security.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/customers/register", "/api/customers/login", "/api/customers/logout").permitAll()
                .anyRequest().permitAll()
            )
            .sessionManagement(session -> session
                .maximumSessions(1)
                .expiredUrl("/login?expired=true")
                .maxSessionsPreventsLogin(true)
            );
        return http.build();
    }

    /**
     * Defines a custom UserDetailsService to load user details from the database.
     *
     * This service retrieves a customer by their username from the database and creates a User object
     * representing the customer for authentication purposes. If the username is not found in the database,
     * a UsernameNotFoundException is thrown.
     *
     * @param customerRepository The repository used to query customer data from the database.
     * @return A UserDetailsService instance that loads user details from the database.
     */
    @Bean
    public UserDetailsService userDetailsService(CustomerRepository customerRepository) {
        return username -> {
            Customer customer = customerRepository.findByUsername(username);
            if (customer == null) {
                throw new UsernameNotFoundException("User not found");
            }
            return User.builder()
                .username(customer.getUsername())
                .password(customer.getPassword())
                .build();
        };
    }
}
