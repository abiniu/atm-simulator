package com.sample.bank.atm.domain;

import java.math.BigDecimal;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Builder(toBuilder = true)
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class Withdrawal {
	private int _50Note;
	private int _20Note;
	private int _10Note;
	private int _5Note;
	private BigDecimal remainingBalance;
	private BigDecimal remainingOverdraftBalance;

	public long getWithdrawAmount() {
		return _50Note * 50 + _20Note * 20 + _10Note * 10 + _5Note * 5;
	}
}
