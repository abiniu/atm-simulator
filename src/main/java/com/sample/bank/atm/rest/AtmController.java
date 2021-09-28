package com.sample.bank.atm.rest;

import javax.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sample.bank.atm.domain.Withdrawal;
import com.sample.bank.atm.domain.projection.BankAccountView;
import com.sample.bank.atm.dto.BankAccountRequestDto;
import com.sample.bank.atm.dto.WithdrawalRequestDto;
import com.sample.bank.atm.service.AccountService;
import com.sample.bank.atm.service.AtmService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class AtmController {
	
	private final AccountService accountService;
	private final AtmService atmMachine;
	
	@PostMapping("/balance")
	public BankAccountView getAccountBalance(@Valid @RequestBody BankAccountRequestDto accountDto) {
		return accountService.getAccountBalance(accountDto.getAccountNumber(), accountDto.getPin());
	}
	
	@PostMapping("/withdraw")
	public Withdrawal withdraw(@Valid @RequestBody WithdrawalRequestDto withdrawalRequestDto) {
		return atmMachine.withdraw(withdrawalRequestDto.getAccountNumber(), withdrawalRequestDto.getPin(), withdrawalRequestDto.getAmmount());
	}

}
