package com.sample.bank.atm.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sample.bank.atm.domain.ATM;

public interface ATMRepository extends JpaRepository<ATM, Long> {

}
