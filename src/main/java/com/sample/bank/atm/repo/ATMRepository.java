package com.sample.bank.atm.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sample.bank.atm.domain.ATM;

public interface ATMRepository extends JpaRepository<ATM, Long> {
	ATM getAtmBySerialNumber(String serialNumber);

	default ATM getATMBySerialNumber() {
		return getAtmBySerialNumber(ATM.VIRTUAL_ATM_SERIAL);
	}
}
