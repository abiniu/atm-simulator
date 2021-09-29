package com.sample.bank.atm.domain.projection;

import java.math.BigDecimal;

public interface BankAccountView {
	public BigDecimal getBalance();
	
	public BigDecimal getOverdraft();

	public String getNumber();
	
	default public BigDecimal getTotalBalance() {
		return getBalance().add(getOverdraft());
	}
}
