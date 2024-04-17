package org.basalt.main.customer.repository;

import org.basalt.main.customer.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

/**
 * @ Author Nephat Muchiri
 * Date 17/04/2024
 */
public interface CustomerRepo extends JpaRepository<Customer, UUID> {
    @Query("FROM Customer c WHERE c.mobileNumber=?1")
    List<Customer> findCustomerByMobile(String mobileNumber);
}
