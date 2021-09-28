package com.sample.bank.atm.service;

import org.springframework.stereotype.Service;

import com.sample.bank.atm.domain.Withdrawal;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AtmService {
	
	public Withdrawal withdraw(String accountNumber, String pin, long ammount){
		return Withdrawal.builder()._50Note(2)._10Note(3)._20Note(5).build();
	}
}

