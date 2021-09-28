package com.sample.bank.atm.service;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.sample.bank.atm.domain.Withdrawal;

class WithdrawalCalculatorTest {

	private WithdrawalCalculator withdrawalCalculator;

	@BeforeEach
	public void setUp() {
		withdrawalCalculator = new WithdrawalCalculator();
	}

	@Test
	void testForZero() {
		Withdrawal calculateSplit = withdrawalCalculator.calculateSplit(0, 0, 0, 0, 0l);
		assertThat(calculateSplit.getTotalAmount()).isEqualTo(0l);
		
		calculateSplit = withdrawalCalculator.calculateSplit(220, 20, 10, 80, 0l);
		assertThat(calculateSplit.getTotalAmount()).isEqualTo(0l);
	}

	@Test
	void testForNoResult() {
		assertThat(withdrawalCalculator.calculateSplit(0, 0, 0, 0, 10l)).isNull();
		assertThat(withdrawalCalculator.calculateSplit(0, 0, 0, 1, 10l)).isNull();
		assertThat(withdrawalCalculator.calculateSplit(1, 1, 1, 1, 100l)).isNull();
		assertThat(withdrawalCalculator.calculateSplit(1, 1, 1, 2, 100l)).isNull();
	}

	void testForAmmount(Withdrawal ammount) {
		Withdrawal calculateSplit = withdrawalCalculator.calculateSplit(ammount.get_50Note(), ammount.get_20Note(),
				ammount.get_10Note(), ammount.get_5Note(), ammount.getTotalAmount());
		assertThat(calculateSplit.getTotalAmount()).isEqualTo(ammount.getTotalAmount());
		assertThat(calculateSplit.get_50Note()).isLessThanOrEqualTo(ammount.get_50Note());
		assertThat(calculateSplit.get_20Note()).isLessThanOrEqualTo(ammount.get_20Note());
		assertThat(calculateSplit.get_10Note()).isLessThanOrEqualTo(ammount.get_10Note());
		assertThat(calculateSplit.get_5Note()).isLessThanOrEqualTo(ammount.get_5Note());
	}

	@Test
	void testFor1500() {
		// €1500 made up of 10 x €50s, 30 x €20s, 30 x €10s and 20 x €5s
		Withdrawal ammount = Withdrawal.builder()._50Note(10)._20Note(30)._10Note(30)._5Note(20).build();
		Withdrawal calculateSplit = withdrawalCalculator.calculateSplit(ammount.get_50Note(), ammount.get_20Note(),
				ammount.get_10Note(), ammount.get_5Note(), ammount.getTotalAmount());
		assertThat(calculateSplit.getTotalAmount()).isEqualTo(ammount.getTotalAmount());
		assertThat(calculateSplit).isEqualTo(ammount);
	}

	@Test
	void testForAmmount() {
		// 3 x €20
		Withdrawal calculateSplit = withdrawalCalculator.calculateSplit(1, 3, 0, 0, 60l);
		assertThat(calculateSplit.getTotalAmount()).isEqualTo(60l);
		assertThat(calculateSplit).isEqualTo(Withdrawal.builder()._20Note(3).build());
		
		calculateSplit = withdrawalCalculator.calculateSplit(0, 0, 5, 2, 60l);
		assertThat(calculateSplit.getTotalAmount()).isEqualTo(60l);
		assertThat(calculateSplit).isEqualTo(Withdrawal.builder()._10Note(5)._5Note(2).build());

		calculateSplit = withdrawalCalculator.calculateSplit(2, 3, 2, 10, 200l);
		assertThat(calculateSplit.getTotalAmount()).isEqualTo(200l);
		assertThat(calculateSplit).isEqualTo(Withdrawal.builder()._50Note(2)._20Note(3)._10Note(2)._5Note(4).build());

		calculateSplit = withdrawalCalculator.calculateSplit(2, 2, 2, 10, 200l);
		assertThat(calculateSplit.getTotalAmount()).isEqualTo(200l);
		assertThat(calculateSplit).isEqualTo(Withdrawal.builder()._50Note(2)._20Note(2)._10Note(2)._5Note(8).build());

		calculateSplit = withdrawalCalculator.calculateSplit(1, 2, 4, 22, 235l);
		assertThat(calculateSplit.getTotalAmount()).isEqualTo(235l);
		assertThat(calculateSplit).isEqualTo(Withdrawal.builder()._50Note(1)._20Note(2)._10Note(4)._5Note(21).build());

	}
}
