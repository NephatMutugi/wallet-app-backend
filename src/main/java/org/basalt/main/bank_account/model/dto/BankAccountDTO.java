package org.basalt.main.bank_account.model.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;


@Data
@Builder
public class BankAccountDTO {

	private long accountNo;
	private String bankCode;
	@NotNull
	@Size(min = 3, max = 15,message = "Invalid Bank Name [ 3-15 characters only ]")
	private String bankName;
	@NotNull
	private BigDecimal balance;

}
