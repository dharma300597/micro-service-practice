package com.learning.loans.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;
import java.util.Map;

@ConfigurationProperties("loans")
@NoArgsConstructor
@Getter
@Setter
public class LoanContactInfoDto {
    private String message;
    private Map<String,String> contactDetails;
    private List<String> onCallSupport;
}
