package com.learning.demo.serviceImpl;

import com.learning.cards.dto.CardsDto;
import com.learning.demo.dto.AccountsDTO;
import com.learning.demo.dto.CustomerDetailsDto;
import com.learning.demo.exception.CustomerNotExistsException;
import com.learning.demo.exception.ResourceNotFoundException;
import com.learning.demo.mapper.AccountMapper;
import com.learning.demo.mapper.CustomerDetailsMapper;
import com.learning.demo.model.Account;
import com.learning.demo.model.Customer;
import com.learning.demo.repository.AccountRepository;
import com.learning.demo.repository.CustomerRepository;
import com.learning.demo.service.CustomerDetails;
import com.learning.demo.service.client.CardsFeignClient;
import com.learning.demo.service.client.LoansFeignClient;
import com.learning.loans.dto.LoansDto;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class CustomerDetailImpl implements CustomerDetails {

    private final CardsFeignClient cardsFeignClient;
    private final LoansFeignClient loansFeignClient;
    private final CustomerRepository customerRepository;
    private final AccountRepository accountRepository;
    /**
     *
     * @param mobileNumber - Input Mobile Number
     * @return Customer Details based on a given mobileNumber
     */
    @Override
    public CustomerDetailsDto fetchCustomerDetails(String mobileNumber) {

        Customer byMobileNumber = customerRepository.findByMobileNumber(mobileNumber).orElseThrow(()-> new CustomerNotExistsException("Customer Not Fount For Given Mobile number "+mobileNumber));
        Account accounts = accountRepository.findByCustomerId(byMobileNumber.getCustomerId()).orElseThrow(
                () -> new ResourceNotFoundException("Account", "customerId", byMobileNumber.getCustomerId().toString())
        );
        CustomerDetailsMapper customerDetailsMapper=new CustomerDetailsMapper();
        CustomerDetailsDto customerDetailsDto = customerDetailsMapper.mapToCustomerDetailsDto(byMobileNumber, new CustomerDetailsDto());
        customerDetailsDto.setAccountsDto(AccountMapper.mapToAccountsDto(accounts, new AccountsDTO()));

        ResponseEntity<CardsDto> cardsDtoResponseEntity = cardsFeignClient.fetchCardDetails(mobileNumber);
        customerDetailsDto.setCardsDto(cardsDtoResponseEntity.getBody());
        ResponseEntity<LoansDto> loansDtoResponseEntity = loansFeignClient.fetchLoanDetails(mobileNumber);
        customerDetailsDto.setLoansDto(loansDtoResponseEntity.getBody());
        return customerDetailsDto;
    }
}
