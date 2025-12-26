package com.entitybank.digital.ussd.repository;

import com.entitybank.digital.ussd.entity.UssdCustomer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UssdCustomerRepository extends JpaRepository<UssdCustomer, String> {}
