package com.learning.demo.service;

import com.learning.demo.dto.CustomerDTO;

public interface AccountService {
    /**
     *
     * @param customerDto - CustomerDto Object
     */
    void createAccount(CustomerDTO customerDto);

    /**
     * @param  customerId - long
     */
    CustomerDTO fetchAccount(long customerId);
    /**
     * @param  mobileNumber - String
     * @param customerDto - CustomerDto Object
     */
    CustomerDTO updateAccount(String mobileNumber,CustomerDTO customerDto);

    /**
     * @param  customerMobileNumber - String
     */
    boolean deleteAccount(String customerMobileNumber);
}
