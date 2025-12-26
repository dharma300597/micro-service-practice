package com.learning.demo.controller;

import com.learning.demo.constants.AccountsConstants;
import com.learning.demo.dto.CustomerDTO;
import com.learning.demo.dto.ResponseDTO;
import com.learning.demo.service.AccountService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/account")
@AllArgsConstructor

public class AccountsController {

    private AccountService accountService;
    @PostMapping("create")
    public ResponseEntity<ResponseDTO> createAccount(@RequestBody CustomerDTO customerDto){
        accountService.createAccount(customerDto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ResponseDTO(AccountsConstants.STATUS_201, AccountsConstants.MESSAGE_201));
    }
    @GetMapping("/{id}")
    public ResponseEntity<CustomerDTO> fetchAccount(@PathVariable long id){
        CustomerDTO customerDTO = accountService.fetchAccount(id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(customerDTO);
    }
    @PutMapping("/{id}")
    public ResponseEntity<CustomerDTO> updateAccount(@PathVariable Long id, @RequestBody CustomerDTO customerDto) {
        CustomerDTO updatedcustomerDTO = accountService.updateAccount(id, customerDto);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(updatedcustomerDTO);
    }
}
