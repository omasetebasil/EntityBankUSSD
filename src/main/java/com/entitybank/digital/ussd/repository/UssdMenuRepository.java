package com.entitybank.digital.ussd.repository;

import com.entitybank.digital.ussd.entity.UssdMenu;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UssdMenuRepository extends JpaRepository<UssdMenu, Long> {
    Optional<UssdMenu> findByMenuCode(String code);
    Optional<UssdMenu> findByParentMenuAndOptionValue(String parent, String option);
}
