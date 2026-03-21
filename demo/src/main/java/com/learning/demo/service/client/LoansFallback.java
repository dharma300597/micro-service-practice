package com.learning.demo.service.client;

import com.learning.cards.dto.CardsDto;
import com.learning.loans.dto.LoansDto;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class LoansFallback implements  LoansFeignClient{

    @Override
    public ResponseEntity<LoansDto> fetchLoanDetails(String mobileNumber, String correlationId) {
        return null;
    }
}
