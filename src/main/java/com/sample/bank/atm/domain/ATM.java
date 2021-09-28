package com.sample.bank.atm.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Version;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class ATM {
	
	@Id
	@GeneratedValue
	private Long id;
	
	private int _50Note;
	private int _20Note;
	private int _10Note;
	private int _5Note;
	
	@Version
	private Long version;
	
}
