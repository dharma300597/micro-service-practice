package com.learning.demo.serviceImpl;

import com.learning.demo.constants.AccountsConstants;
import com.learning.demo.dto.CustomerDTO;
import com.learning.demo.exception.CustomerAlreadyExistsException;
import com.learning.demo.exception.CustomerNotExistsException;
import com.learning.demo.mapper.CustomerMapper;
import com.learning.demo.model.Account;
import com.learning.demo.model.Customer;
import com.learning.demo.repository.AccountRepository;
import com.learning.demo.repository.CustomerRepository;
import com.learning.demo.service.AccountService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Random;

@Service
@AllArgsConstructor  //Using All args constructor will make this class constructor injection
public class AccountServiceImpl  implements AccountService {

    private AccountRepository accountsRepository;
    private CustomerRepository customerRepository;

    private final Logger logger= LoggerFactory.getLogger(AccountServiceImpl.class);

    /**
     *
     * @param customerDto - CustomerDto Object
     */
    @Override
    public void createAccount(CustomerDTO customerDto) {
        Customer customer = CustomerMapper.mapToCustomer(customerDto, new Customer());
        Optional<Customer> isCustomerExist= customerRepository.findByMobileNumber(customer.getMobileNumber());
        if(isCustomerExist.isPresent()){
            throw new CustomerAlreadyExistsException("Customer already registered with given mobileNumber "
                    +customerDto.getMobileNumber());
        }
        //While using cascadeType(ALL,PERSIST) in entity we can comment out below explicit account entity saving logic.if you prefer not giving cascadeType then you must explicitly save account first then only customer to be saved else you get exception
        Account account = accountsRepository.save(createNewAccount(customer));
        customer.setAccount(account);
        customerRepository.save(customer);
    }

    /**
     * @param customerId - long
     */
    @Override
    public CustomerDTO fetchAccount(long customerId) {
        Optional<Customer> customerById = customerRepository.findById(customerId);
        if(customerById.isEmpty()){
            logger.error("\"Customer not found for given ID \""+customerId);
            throw new CustomerNotExistsException("Customer not found for given ID "+customerId);
        }
        return CustomerMapper.mapToCustomerDto(customerById.get(), new CustomerDTO());
    }

    /**
     * @param customerId  - long
     * @param customerDto - CustomerDto Object
     */
    @Override
    @Transactional
    public CustomerDTO updateAccount(long customerId, CustomerDTO customerDto) {
        Optional<Customer> customerById = customerRepository.findById(customerId);
        if(customerById.isEmpty()){
            logger.error("\"Customer not found for given ID \""+customerId);
            throw new CustomerNotExistsException("Customer not found for given ID "+customerId);
        }
        Customer customer = customerById.get();
        customer.setName(customerDto.getName());
        customer.setEmail(customerDto.getEmail());
        customer.setMobileNumber(customerDto.getMobileNumber());
        Customer savedCustomer = customerRepository.save(customer);
        customer.getAccount().setAccountType("CURRENT");
        return CustomerMapper.mapToCustomerDto(savedCustomer,new CustomerDTO());
    }


    /**
     * @param customer - Customer Object
     * @return the new account details
     */
    private Account createNewAccount(Customer customer) {
        Account newAccount = new Account();
//        newAccount.setCustomerId(customer);
        long randomAccNumber = 1000000000L + new Random().nextInt(900000000);

        newAccount.setAccountNumber(randomAccNumber);
        newAccount.setAccountType(AccountsConstants.SAVINGS);
        newAccount.setBranchAddress(AccountsConstants.ADDRESS);
        return newAccount;
    }
}
