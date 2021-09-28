package com.sample.bank.atm.domain.projection;

import java.math.BigDecimal;

public interface BankAccountView {
	public BigDecimal getBalance();

	public String getNumber();
}
