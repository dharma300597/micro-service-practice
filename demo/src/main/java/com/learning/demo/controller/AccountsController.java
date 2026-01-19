package com.learning.demo.controller;

import com.learning.demo.constants.AccountsConstants;
import com.learning.demo.dto.CustomerDTO;
import com.learning.demo.dto.ErrorResponseDTO;
import com.learning.demo.dto.ResponseDTO;
import com.learning.demo.service.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/account")
@AllArgsConstructor
@Tag(name = "Account and Customer related endpoints",
description = "Here we have end-points for account crud ")
@Validated
public class AccountsController {

    private AccountService accountService;

    @Operation(
            summary = "Account and Customer creating",
            description = "Account and Customer would be created using this API"
    )
    @PostMapping("create")
    public ResponseEntity<ResponseDTO> createAccount(@Valid @RequestBody CustomerDTO customerDto){
        accountService.createAccount(customerDto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ResponseDTO(AccountsConstants.STATUS_201, AccountsConstants.MESSAGE_201));
    }

    @Operation(
            summary = "Account and Customer fetching",
            description = "Account and Customer would be fetched using this API"
    )
    @GetMapping("/{id}")
    public ResponseEntity<CustomerDTO> fetchAccount(@PathVariable long id){
        CustomerDTO customerDTO = accountService.fetchAccount(id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(customerDTO);
    }
    @Operation(
            summary = "Account and Customer Updating",
            description = "Account and Customer would be Updating using this API"
    )
    @PutMapping("/{mobileNumber}")
    public ResponseEntity<CustomerDTO> updateAccount(@Pattern(regexp="(^$|[0-9]{10})",message = "Mobile number must be 10 digits") @PathVariable String mobileNumber, @RequestBody CustomerDTO customerDto) {
        CustomerDTO updatedcustomerDTO = accountService.updateAccount(mobileNumber, customerDto);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(updatedcustomerDTO);
    }

    @Operation(
            summary = "Account and Customer deleting",
            description = "Account and Customer would be deleted using this API"
    )
    @ApiResponses({
            @ApiResponse(
                responseCode ="200",
                description ="Account data deleted Successfully"
            ),
            @ApiResponse(
                    responseCode = "417",
                    description = "Account Data Not Deleted.Please Contact dev team",
                    content = @Content(
                            schema = @Schema(
                                    implementation = ErrorResponseDTO.class
                            )
                    )

            )
    })
    @DeleteMapping()
    public ResponseEntity<ResponseDTO> deleteAccount(@RequestParam String mobileNo) {
        accountService.deleteAccount(mobileNo);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResponseDTO(AccountsConstants.STATUS_200, AccountsConstants.MESSAGE_200));
    }
}
