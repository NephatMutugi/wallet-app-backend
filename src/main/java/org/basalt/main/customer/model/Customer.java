package org.basalt.main.customer.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.basalt.main.common.config.entityaudit.EntityAudit;

import java.util.UUID;

/**
 * @ Author Nephat Muchiri
 * Date 17/04/2024
 */
@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Customer extends EntityAudit {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "customerId", nullable = false, unique = true)
    private UUID customerId;

    @NotNull
    @Size(min = 3, message = "Invalid Customer name [ contains at least 3 characters ]")
    private String customerName;

    @NotNull
    @Size(min = 10, max = 13 ,message = "Invalid Mobile Number [ 10 Digit Only ] ")
    @Column(unique = true)
    private String mobileNumber;

    @NotNull
    @Size(max = 255 ,message = "Invalid Email Address")
    @Column(unique = true)
    private String email;

    @NotNull
    @Size(min = 6, max = 12, message = "Invalid Password [ must be 6 to 12 characters ]")
    private String password;


}