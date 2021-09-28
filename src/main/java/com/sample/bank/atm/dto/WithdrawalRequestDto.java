package com.sample.bank.atm.dto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class WithdrawalRequestDto extends BankAccountRequestDto {
	@NotNull
	@Min(5)
	private Long ammount;
}
