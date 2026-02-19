package com.learning.demo.service.client;

import com.learning.cards.dto.CardsDto;
import jakarta.validation.constraints.Pattern;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("cards")
public interface CardsFeignClient {

    @GetMapping("api/cards/fetch")
    public ResponseEntity<CardsDto> fetchCardDetails(@RequestParam String mobileNumber,@RequestHeader("bank-correlation-id") String correlationId);
}
