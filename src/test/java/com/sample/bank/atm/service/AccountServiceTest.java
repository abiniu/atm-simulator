package com.sample.bank.atm.service;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.sample.bank.atm.domain.BankAccount;
import com.sample.bank.atm.domain.projection.BankAccountView;
import com.sample.bank.atm.repo.AccountRepository;

@SpringBootTest
@Transactional
class AccountServiceTest {

	@Autowired
	AccountService accountService;

	@Autowired
	AccountRepository accountRepository;

	@Test
	void invalidAccount() {
		ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
			accountService.getAccountBalance("122", "1212");
		});
		assertThat(exception.getStatus()).isEqualTo(HttpStatus.NOT_FOUND);
		assertThat(exception.getReason()).isEqualTo("Account not found");

		exception = assertThrows(ResponseStatusException.class, () -> {
			accountService.getAccountBalance("123456789", "1212");
		});
		assertThat(exception.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST);
		assertThat(exception.getReason()).isEqualTo("The provided PIN is invalid");
	}

	@Test
	void accountBalance() {
		BankAccountView accountBalance = accountService.getAccountBalance("123456789", "1234");
		assertThat(accountBalance.getBalance()).isEqualByComparingTo(BigDecimal.valueOf(800l));
		assertThat(accountBalance.getOverdraft()).isEqualByComparingTo(BigDecimal.valueOf(200l));
		assertThat(accountBalance.getTotalBalance()).isEqualByComparingTo(BigDecimal.valueOf(1000l));
		assertThat(accountBalance.getNumber()).isEqualTo("123456789");
	}
	
	@Test
	void accountForWithdraw() {
		BankAccount accountBalance = accountService.getAccountForWithdraw("123456789", "1234", 200l);
		assertThat(accountBalance.getBalance()).isEqualByComparingTo(BigDecimal.valueOf(800l));
		assertThat(accountBalance.getOverdraft()).isEqualByComparingTo(BigDecimal.valueOf(200l));
		assertThat(accountBalance.getTotalBalance()).isEqualByComparingTo(BigDecimal.valueOf(1000l));
		assertThat(accountBalance.getNumber()).isEqualTo("123456789");
	}
	
	@Test
	void invalidAccountForWithdraw() {
		ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
			accountService.getAccountForWithdraw("122", "1212", 100l);
		});
		assertThat(exception.getStatus()).isEqualTo(HttpStatus.NOT_FOUND);
		assertThat(exception.getReason()).isEqualTo("Account not found");

		exception = assertThrows(ResponseStatusException.class, () -> {
			accountService.getAccountForWithdraw("123456789", "1212", 100l);
		});
		assertThat(exception.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST);
		assertThat(exception.getReason()).isEqualTo("The provided PIN is invalid");
		
		exception = assertThrows(ResponseStatusException.class, () -> {
			accountService.getAccountForWithdraw("123456789", "1234", 2000l);
		});
		assertThat(exception.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST);
		assertThat(exception.getReason()).isEqualTo("Not enought funds for account: 123456789");
	}

}
