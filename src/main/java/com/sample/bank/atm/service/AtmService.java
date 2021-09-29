package com.sample.bank.atm.service;

import java.math.BigDecimal;

import javax.persistence.OptimisticLockException;

import org.springframework.http.HttpStatus;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.sample.bank.atm.domain.ATM;
import com.sample.bank.atm.domain.BankAccount;
import com.sample.bank.atm.domain.Withdrawal;
import com.sample.bank.atm.repo.ATMRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class AtmService {

	private final WithdrawalCalculator withdrawalCalculator;
	private final ATMRepository atmRepository;
	private final AccountService accountService;

	@Transactional
	@Retryable(OptimisticLockException.class)
	public Withdrawal withdraw(String accountNumber, String pin, long ammount) {
		BankAccount accountForWithdraw = accountService.getAccountForWithdraw(accountNumber, pin, ammount);
		ATM atm = atmRepository.getATMBySerialNumber();
		Withdrawal calculatedSplit = withdrawalCalculator.calculateSplit(atm.get_50Note(), atm.get_20Note(),
				atm.get_10Note(), atm.get_5Note(), ammount);
		if (calculatedSplit != null) {
			adjustAccount(ammount, accountForWithdraw);
			adjustAtmNotes(atm, calculatedSplit);
			accountService.save(accountForWithdraw);
			atmRepository.save(atm);
			calculatedSplit.setRemainingBalance(accountForWithdraw.getBalance());
			calculatedSplit.setRemainingOverdraftBalance(accountForWithdraw.getOverdraft());
		} else {
			log.error(
					"ATM does not have enought notes for windrawal, requested account number: {}, requested ammount: {}",
					accountNumber, ammount);
			throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY,
					"ATM does not have enought notes for windrawal");
		}
		return calculatedSplit;
	}

	protected void adjustAccount(long ammount, BankAccount accountForWithdraw) {
		BigDecimal newBallance = accountForWithdraw.getBalance().subtract(BigDecimal.valueOf(ammount));
		if (newBallance.compareTo(BigDecimal.ZERO) < 0) {
			log.info("Ballance for account: {} is empty, using overdraft", accountForWithdraw.getNumber());
			accountForWithdraw.setBalance(BigDecimal.ZERO);
			accountForWithdraw.setOverdraft(accountForWithdraw.getOverdraft().add(newBallance));
		} else {
			accountForWithdraw.setBalance(newBallance);
		}
	}

	protected void adjustAtmNotes(ATM atm, Withdrawal calculatedSplit) {
		atm.set_50Note(atm.get_50Note() - calculatedSplit.get_50Note());
		atm.set_20Note(atm.get_20Note() - calculatedSplit.get_20Note());
		atm.set_10Note(atm.get_10Note() - calculatedSplit.get_10Note());
		atm.set_5Note(atm.get_5Note() - calculatedSplit.get_5Note());
	}
}
