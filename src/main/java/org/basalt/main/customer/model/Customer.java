package org.basalt.main.customer.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.basalt.main.common.config.entityaudit.EntityAudit;

import java.time.LocalDateTime;
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "customer_id", updatable = false, nullable = false)
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
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;
    private String address;
    private String dateOfBirth;
    private String nationalId;
    private String accountStatus;
    private LocalDateTime lastLoginDate;

}