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
     * @param  customerId - long
     * @param customerDto - CustomerDto Object
     */
    CustomerDTO updateAccount(long customerId,CustomerDTO customerDto);
}
