package com.learning.demo.mapper;

import com.learning.demo.dto.AccountsDTO;
import com.learning.demo.dto.CustomerDTO;
import com.learning.demo.model.Account;
import com.learning.demo.model.Customer;

public class CustomerMapper {
    public static CustomerDTO mapToCustomerDto(Customer customer, CustomerDTO customerDto) {
        customerDto.setName(customer.getName());
        customerDto.setEmail(customer.getEmail());
        customerDto.setMobileNumber(customer.getMobileNumber());
        if (customer.getAccount() != null){
            customerDto.setAccountsDto(AccountMapper.mapToAccountsDto(customer.getAccount(),new AccountsDTO()));
        }
        return customerDto;
    }

    public static Customer mapToCustomer(CustomerDTO customerDto, Customer customer) {
        customer.setName(customerDto.getName());
        customer.setEmail(customerDto.getEmail());
        customer.setMobileNumber(customerDto.getMobileNumber());
        if(customerDto.getAccountsDto() !=null){
            customer.setAccount(AccountMapper.mapToAccounts(customerDto.getAccountsDto(),new Account()));
        }
        return customer;
    }
}
