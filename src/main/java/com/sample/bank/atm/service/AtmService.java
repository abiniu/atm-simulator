package com.sample.bank.atm.service;

import java.math.BigDecimal;

import javax.transaction.Transactional;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
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
	public Withdrawal withdraw(String accountNumber, String pin, long ammount) {
		BankAccount accountForWithdraw = accountService.getAccountForWithdraw(accountNumber, pin, ammount);
		ATM atm = atmRepository.getATMBySerialNumber();
		Withdrawal calculatedSplit = withdrawalCalculator.calculateSplit(atm.get_50Note(), atm.get_20Note(),
				atm.get_10Note(), atm.get_5Note(), ammount);
		if (calculatedSplit != null) {

			BigDecimal newBallance = accountForWithdraw.getBalance().subtract(BigDecimal.valueOf(ammount));
			if (newBallance.compareTo(BigDecimal.ZERO) < 0) {
				accountForWithdraw.setBalance(BigDecimal.ZERO);
				accountForWithdraw.setOverdraft(accountForWithdraw.getOverdraft().add(newBallance));
			} else {
				accountForWithdraw.setBalance(newBallance);
			}
			accountService.save(accountForWithdraw);
			atmRepository.save(atm);

		} else {
			log.error(
					"ATM does not have enought notes for windrawal, requested account number: {}, requested ammount: {}",
					accountNumber, ammount);
			throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY,
					"ATM does not have enought notes for windrawal");
		}
		return calculatedSplit;
	}
}
