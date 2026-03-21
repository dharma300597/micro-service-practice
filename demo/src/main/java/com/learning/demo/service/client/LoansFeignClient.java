package com.learning.demo.service.client;

import com.learning.loans.dto.LoansDto;
import jakarta.validation.constraints.Pattern;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "loans",fallback = LoansFallback.class)
public interface LoansFeignClient {
    @GetMapping("/api/loans/fetch")
    ResponseEntity<LoansDto> fetchLoanDetails(@RequestParam String mobileNumber,@RequestHeader("bank-correlation-id") String correlationId);
}
