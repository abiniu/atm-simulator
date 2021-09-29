package com.sample.bank.atm.service;

import org.springframework.stereotype.Service;

import com.sample.bank.atm.domain.Withdrawal;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class WithdrawalCalculator {

	public Withdrawal calculateSplit(int _50Note, int _20Note, int _10Note, int _5Note, Long ammomunt) {
		return calculateSplit(Withdrawal.builder().build(), _50Note, _20Note, _10Note, _5Note, ammomunt);
	}
	
	private Withdrawal calculateSplit(Withdrawal withdrawal, int _50Note, int _20Note, int _10Note, int _5Note, Long ammomunt) {
		if(ammomunt == 0l ) {
			log.debug("Found the notes split: {}", withdrawal);
			return withdrawal;
		}
		
		if (_50Note * 50 + _20Note * 20 + _10Note * 10 + _5Note * 5 < ammomunt) {
			log.trace("no point of searching, not enough money for this search path");
			return null;
		}
		
		Withdrawal newWithdrawal = withdrawal.toBuilder().build();
		
		if (ammomunt >= 50 && _50Note > 0) {
			newWithdrawal.set_50Note(newWithdrawal.get_50Note() + 1);
			Withdrawal calculateSplit = calculateSplit(newWithdrawal, _50Note - 1, _20Note, _10Note, _5Note, ammomunt - 50l);
			if (calculateSplit != null) {
				return calculateSplit;
			}
			newWithdrawal = withdrawal.toBuilder().build();
		}
		if (ammomunt >= 20 && _20Note > 0) {
			newWithdrawal.set_20Note(newWithdrawal.get_20Note() + 1);
			Withdrawal calculateSplit = calculateSplit(newWithdrawal, _50Note, _20Note - 1, _10Note, _5Note, ammomunt - 20l);
			if (calculateSplit != null) {
				return calculateSplit;
			}
			newWithdrawal = withdrawal.toBuilder().build();
		}
		if (ammomunt >= 10 && _10Note > 0) {
			newWithdrawal.set_10Note(newWithdrawal.get_10Note() + 1);
			Withdrawal calculateSplit = calculateSplit(newWithdrawal, _50Note, _20Note, _10Note - 1, _5Note, ammomunt - 10l);
			if (calculateSplit != null) {
				return calculateSplit;
			}
			newWithdrawal = withdrawal.toBuilder().build();
		}
		if (ammomunt >= 5 && _5Note > 0) {
			newWithdrawal.set_5Note(newWithdrawal.get_5Note() + 1);
			Withdrawal calculateSplit = calculateSplit(newWithdrawal, _50Note, _20Note, _10Note, _5Note - 1, ammomunt - 5l);
			if (calculateSplit != null) {
				return calculateSplit;
			}
			newWithdrawal = withdrawal.toBuilder().build();
		}
		
		return null;
		
	}
}
