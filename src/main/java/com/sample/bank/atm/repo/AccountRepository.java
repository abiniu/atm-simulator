package com.sample.bank.atm.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.sample.bank.atm.domain.BankAccount;
import com.sample.bank.atm.domain.projection.BankAccountView;

public interface AccountRepository extends JpaRepository<BankAccount, Long> {
	BankAccountView getAccountByNumberAndPin(String number, String pin);

	BankAccountView getAccountByNumber(String number);

	@Query("Select ac from BankAccount ac Where ac.number = ?1 And ac.pin = ?2")
	BankAccount getAccountForWithdraw(String number, String pin);
}
