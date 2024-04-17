package org.basalt.main.customer.repository;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.basalt.main.customer.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.UUID;

/**
 * @ Author Nephat Muchiri
 * Date 17/04/2024
 */
public interface CustomerRepo extends JpaRepository<Customer, UUID> {
    @Query("FROM Customer c WHERE c.mobileNumber=?1")
    Customer findCustomerByMobileNumber(String mobileNumber);

    Customer findCustomerByMobileNumberOrEmailOrNationalId(@NotNull @Size(min = 10, max = 13, message = "Invalid Mobile Number [ 10 Digit Only ] ") String mobileNumber, @NotNull @Size(max = 255, message = "Invalid Email Address") String email, String nationalId);
}
