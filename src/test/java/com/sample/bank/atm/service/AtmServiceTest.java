package com.sample.bank.atm.service;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.sample.bank.atm.domain.ATM;
import com.sample.bank.atm.domain.BankAccount;
import com.sample.bank.atm.domain.Withdrawal;
import com.sample.bank.atm.domain.projection.BankAccountView;
import com.sample.bank.atm.repo.ATMRepository;
import com.sample.bank.atm.repo.AccountRepository;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
class AtmServiceTest {

	@Autowired
	AtmService atmService;
	
	@Autowired
	ATMRepository atmRepository;
	
	@Autowired
	AccountRepository accountRepository;
	
	@Test
	void withdraw() {
		ATM atm = atmRepository.getATMBySerialNumber();
		assertThat(atm.get_50Note()).isGreaterThan(0);
		
		Withdrawal withdraw = atmService.withdraw("123456789", "1234", 900l);
		assertThat(withdraw).isNotNull();
		assertThat(withdraw.getWithdrawAmount()).isEqualTo(900l);
		assertThat(withdraw.getRemainingBalance()).isEqualByComparingTo(BigDecimal.ZERO);
		assertThat(withdraw.getRemainingOverdraftBalance()).isEqualByComparingTo(BigDecimal.valueOf(100l));
		
		ATM afterAtm = atmRepository.getATMBySerialNumber();
		assertThat(afterAtm.get_50Note()).isEqualTo(0);
		assertThat(afterAtm.get_20Note()).isEqualTo(10);
		assertThat(afterAtm.get_10Note()).isEqualTo(30);
		assertThat(afterAtm.get_5Note()).isEqualTo(20);
		BankAccountView account = accountRepository.getAccountByNumber("123456789");
		assertThat(account.getBalance()).isEqualByComparingTo(BigDecimal.ZERO);
		assertThat(account.getOverdraft()).isEqualByComparingTo(BigDecimal.valueOf(100l));
	}
	
	@Test
	void withdrawAll() {
		BankAccount account = accountRepository.getAccountForWithdraw("123456789", "1234");
		account.setBalance(BigDecimal.valueOf(1500l));
		accountRepository.save(account);
		
		ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
			atmService.withdraw("123456789", "1234", 1600l);
		});
		
		assertThat(exception.getStatus()).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY);
		assertThat(exception.getReason()).isEqualTo("ATM does not have enought notes for windrawal");
	}
	
	@Test
	void invalidAccountForWithdraw() {
		ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
			atmService.withdraw("122", "1212", 100l);
		});
		assertThat(exception.getStatus()).isEqualTo(HttpStatus.NOT_FOUND);
		assertThat(exception.getReason()).isEqualTo("Account not found");

		exception = assertThrows(ResponseStatusException.class, () -> {
			atmService.withdraw("123456789", "1212", 100l);
		});
		assertThat(exception.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST);
		assertThat(exception.getReason()).isEqualTo("The provided PIN is invalid");
		
		exception = assertThrows(ResponseStatusException.class, () -> {
			atmService.withdraw("123456789", "1234", 2000l);
		});
		assertThat(exception.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST);
		assertThat(exception.getReason()).isEqualTo("Not enought funds for account: 123456789");
	}

}
