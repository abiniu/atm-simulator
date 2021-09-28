package com.sample.bank.atm.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sample.bank.atm.domain.BankAccount;
import com.sample.bank.atm.domain.projection.BankAccountView;

public interface AccountRepository extends JpaRepository<BankAccount, Long> {
	BankAccountView getAccountByNumberAndPin(String number, String pin);
}
