package org.basalt.main.customer.service;

import org.basalt.main.customer.model.Customer;

import java.security.SecureRandom;
import java.util.Base64;

/**
 * @ Author Nephat Muchiri
 * Date 17/04/2024
 */
public class ServiceUtils {

    // Secure Random generator for token creation
    private static final SecureRandom secureRandom = new SecureRandom();

    // Utility method to generate a secure token
    public static String generateSecureToken() {
        byte[] randomBytes = new byte[24];
        secureRandom.nextBytes(randomBytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes);
    }

    // Utility method to validate password
    public static boolean isPasswordValid(String providedPassword, String storedPassword) {
        // Here you can add hashing and salting logic if passwords are stored hashed
        return providedPassword.equals(storedPassword);
    }

    public static Customer setUpdatedFields(Customer request, Customer existingCustomer) {

        if (request.getCustomerName() != null) {
            existingCustomer.setCustomerName(request.getCustomerName());
        }

        if (request.getMobileNumber() != null) {
            existingCustomer.setMobileNumber(request.getMobileNumber());
        }

        if (request.getEmail() != null) {
            existingCustomer.setEmail(request.getEmail());
        }

        if (request.getAddress() != null) {
            existingCustomer.setAddress(request.getAddress());
        }
        return existingCustomer;
    }
}
