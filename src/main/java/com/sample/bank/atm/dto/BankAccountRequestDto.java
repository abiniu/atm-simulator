package com.sample.bank.atm.dto;

import javax.validation.constraints.NotEmpty;

import lombok.Data;

@Data
public class BankAccountRequestDto {
	@NotEmpty
	private String accountNumber;
	@NotEmpty
	private String pin;
}