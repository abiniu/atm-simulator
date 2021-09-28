package com.sample.bank.atm.service;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import com.sample.bank.atm.domain.projection.BankAccountView;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class AccountService {

	public BankAccountView getAccountBalance(String accountNumber, String pin) {
		return new BankAccountView() {

			@Override
			public BigDecimal getBalance() {
				return new BigDecimal(0.0);
			}

			@Override
			public String getNumber() {
				return accountNumber;
			}

		};
	}
}
