package org.basalt.main.beneficiary.model;

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
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Beneficiary extends EntityAudit {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false, unique = true)
    private UUID id;

    @NotNull
    @Size(min = 10, max = 13 ,message = "Invalid Mobile Number [ 10 Digit Only ] ")
    private String beneficiaryMobileNumber;

    @NotNull
    @Size(min = 3, message = "Invalid Beneficiary name [ contains at least 3 characters ]")
    private String beneficiaryName;


    @Column(name = "walletId")
    private UUID walletId;

    public Beneficiary(String beneficiaryMobileNumber, String beneficiaryName) {
        this.beneficiaryMobileNumber = beneficiaryMobileNumber;
        this.beneficiaryName = beneficiaryName;
    }
}
