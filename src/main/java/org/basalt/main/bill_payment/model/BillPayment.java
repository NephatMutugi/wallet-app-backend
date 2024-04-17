package org.basalt.main.bill_payment.model;

import jakarta.persistence.*;
import lombok.*;
import org.basalt.main.common.config.entityaudit.EntityAudit;
import org.basalt.main.wallet.model.Wallet;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class BillPayment extends EntityAudit {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Column(name = "billId", nullable = false, unique = true)
	private UUID billId;
	
	private BigDecimal amount;
	
	private String billType;
	
	private LocalDate billPaymentDate;
	
	@ManyToOne(cascade = CascadeType.ALL)
	private Wallet wallet;

	public BillPayment(BigDecimal amount, String billType, LocalDate billPaymentDate) {
		this.amount = amount;
		this.billType = billType;
		this.billPaymentDate = billPaymentDate;
	}
}
