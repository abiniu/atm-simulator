package com.sample.bank.atm.domain;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Version;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class BankAccount {

	@Id
	@GeneratedValue
	private Long id;
	
	private String number;
	
	private String pin;
	
	@Column(precision = 10, scale = 2)
	private BigDecimal balance;
	
	@Column(precision = 10, scale = 2)
	private BigDecimal overdraft;
	
	@Version
	private Long version;
	
	public BigDecimal getTotalBalance() {
		return  balance.add(overdraft);
	}
	
	public boolean hasFunds(BigDecimal funds) {
		return balance.subtract(funds).add(overdraft).compareTo(BigDecimal.ZERO) >= 0;
	}
	
}
