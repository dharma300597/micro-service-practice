package com.learning.demo.service;

import com.learning.demo.dto.CustomerDetailsDto;

public interface CustomerDetails {
    /**
     *
     * @param mobileNumber - Input Mobile Number
     * @return Customer Details based on a given mobileNumber
     */
    CustomerDetailsDto fetchCustomerDetails(String mobileNumber,String correlationId);
}
