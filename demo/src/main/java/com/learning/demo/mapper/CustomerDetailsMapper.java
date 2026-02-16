package com.learning.demo.mapper;


import com.learning.demo.dto.CustomerDTO;
import com.learning.demo.dto.CustomerDetailsDto;
import com.learning.demo.model.Customer;

public class CustomerDetailsMapper {
    public static CustomerDetailsDto mapToCustomerDetailsDto(Customer customer, CustomerDetailsDto customerDetailsDTO){
        customerDetailsDTO.setName(customer.getName());
        customerDetailsDTO.setEmail(customer.getEmail());
        customerDetailsDTO.setMobileNumber(customer.getMobileNumber());
        return customerDetailsDTO;
    }
}
