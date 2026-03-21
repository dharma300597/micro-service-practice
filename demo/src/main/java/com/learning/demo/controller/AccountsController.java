package com.learning.demo.controller;

import com.learning.demo.constants.AccountsConstants;
import com.learning.demo.dto.*;
import com.learning.demo.service.AccountService;
import com.learning.demo.service.CustomerDetails;
import io.github.resilience4j.retry.annotation.Retry;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/account")
@Tag(name = "Account and Customer related endpoints",
description = "Here we have end-points for account crud ")
@Validated
public class AccountsController {

    private static final Logger logger= LoggerFactory.getLogger(AccountsController.class);

    private AccountService accountService;

    private CustomerDetails customerDetails;

    @Value("${build.version}")
    private String buildVersion;

    private Environment environment;

    @Autowired
    private AccountContactInfoDto accountContactInfoDto;

    public AccountsController(AccountService accountService,Environment environment,CustomerDetails customerDetails){
        this.accountService=accountService;
        this.environment=environment;
        this.customerDetails=customerDetails;
    }
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

    @Operation(
            summary = "Account Service version",
            description = "Fetching Account Service build version"
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

            )}
    )
    @Retry(name = "getBuildInfo",fallbackMethod = "getBuildInfoFallback")
    @GetMapping("build-version")
    public ResponseEntity<?> getBuildVersion(){
        logger.debug("build-version api call is invoked");
        throw new RuntimeException("Dummy exception");
        //return ResponseEntity.status(HttpStatus.OK).body(buildVersion);
    }


    @GetMapping("java-version")
    public ResponseEntity<String> getJavaVersion(){
        return ResponseEntity.status(HttpStatus.OK).body(environment.getProperty("JAVA_HOME"));
    }

    @Operation(
            summary = "Account Service version",
            description = "Fetching Contact info "
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode ="200",
                    description ="Contact info data fetched Successfully"
            ),
            @ApiResponse(
                    responseCode = "417",
                    description = "Contact info Data Not fetched.Please Contact dev team",
                    content = @Content(
                            schema = @Schema(
                                    implementation = ErrorResponseDTO.class
                            )
                    )

            )}
    )
    @GetMapping("contact-info")
    public ResponseEntity<AccountContactInfoDto> getContactInfo(){
        return ResponseEntity.status(HttpStatus.OK).body(accountContactInfoDto);
    }

    @Operation(
            summary = "Account Service version",
            description = "Fetching customer detail "
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode ="200",
                    description ="customer detail data fetched Successfully"
            ),
            @ApiResponse(
                    responseCode = "417",
                    description = "customer detail Data Not fetched.Please Contact dev team",
                    content = @Content(
                            schema = @Schema(
                                    implementation = ErrorResponseDTO.class
                            )
                    )

            )}
    )
    @GetMapping("customer-details")
    public ResponseEntity<CustomerDetailsDto> getCustomerDetail(
            @RequestHeader("bank-correlation-id") String correlationId,
            @RequestParam
            @Pattern(regexp="(^$|[0-9]{10})",message = "Mobile number must be 10 digits") String mobileNumber){
        logger.debug("Bank Correlation-id found {} ",correlationId);
        CustomerDetailsDto customerDetailsDto = customerDetails.fetchCustomerDetails(mobileNumber,correlationId);
        return ResponseEntity.status(HttpStatus.FOUND).body(customerDetailsDto);
    }

    //This is fall back method for retry
    public ResponseEntity<String> getBuildInfoFallback(Throwable throwable) {
        logger.debug("getBuildInfoFallback() method Invoked");
        return ResponseEntity
                .status(HttpStatus.OK)
                .body("0.9");
    }
}
