package com.learning.demo.repository;

import com.learning.demo.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface AccountRepository  extends JpaRepository<Account,Long> {
    @Query("SELECT o FROM Account o WHERE o.customerId.customerId = :customerId")
    Optional<Account> findByCustomerId(@Param("customerId") Long customerId);

}
