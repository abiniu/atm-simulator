package com.sample.bank.atm.service;

import java.math.BigDecimal;


import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.sample.bank.atm.domain.BankAccount;
import com.sample.bank.atm.domain.projection.BankAccountView;
import com.sample.bank.atm.repo.AccountRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Service
@Slf4j
public class AccountService {

	private final AccountRepository accountRepository;

	public BankAccountView getAccountBalance(String accountNumber, String pin) {
		BankAccountView bankAccountView = accountRepository.getAccountByNumberAndPin(accountNumber, pin);
		validateAccount(accountNumber, bankAccountView);
		return bankAccountView;
	}

	public BankAccount getAccountForWithdraw(String accountNumber, String pin, long ammount) {
		BankAccount bankAccount = accountRepository.getAccountForWithdraw(accountNumber, pin);
		validateAccount(accountNumber, bankAccount);
		if (!bankAccount.hasFunds(BigDecimal.valueOf(ammount))) {
			log.warn("Not enought funds, account ballance: {}, account number: {}, requested ammount: {}",
					bankAccount.getTotalBalance(), accountNumber, ammount);
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					"Not enought funds for account: " + accountNumber);
		}
		return bankAccount;
	}

	@Transactional
	public BankAccount save(BankAccount bankAccount) {
		return accountRepository.save(bankAccount);
	}

	private void validateAccount(String accountNumber, Object bankAccount) {
		if (bankAccount == null && accountRepository.getAccountByNumber(accountNumber) != null) {
			log.warn("Invalid PIN number for account: {}", accountNumber);
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The provided PIN is invalid");
		} else if (bankAccount == null) {
			log.warn("Account: {} not found", accountNumber);
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found");
		}
	}
}
