package com.entitybank.digital.ussd.repository;

import com.entitybank.digital.ussd.entity.UssdTransition;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UssdTransitionRepository
        extends JpaRepository<UssdTransition, Long> {

    List<UssdTransition> findByFromScreenOrderByPriorityDesc(String fromScreen);
}

