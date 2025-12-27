package com.entitybank.digital.ussd.repository;

import com.entitybank.digital.ussd.entity.UssdScreen;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UssdScreenRepository
        extends JpaRepository<UssdScreen, String> {}

