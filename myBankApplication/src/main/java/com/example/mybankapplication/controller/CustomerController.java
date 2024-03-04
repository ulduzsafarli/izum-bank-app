package com.example.mybankapplication.controller;

import com.example.mybankapplication.model.customers.CustomerFilterDto;
import com.example.mybankapplication.model.customers.CustomerResponse;
import com.example.mybankapplication.model.customers.CustomerRequest;
import com.example.mybankapplication.service.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/customers")
@RequiredArgsConstructor
public class CustomerController {
    private final CustomerService customerService;

    @GetMapping("/search")
    public Page<CustomerResponse> getCustomersByFilter(@Valid CustomerFilterDto customerFilterDto, Pageable pageable) {
        return customerService.findCustomerByFilter(customerFilterDto, pageable);
    }

    @GetMapping("/search/{id}")
    public ResponseEntity<CustomerResponse> getCustomerById(@PathVariable Long id) {
        return ResponseEntity.ok(customerService.getCustomerById(id));
    }

    @GetMapping("/search/email/{email}")
    public ResponseEntity<CustomerResponse> getCustomerByEmail(@Valid @PathVariable String email) {
        return ResponseEntity.ok(customerService.getCustomerByEmail(email));
    }

    @GetMapping("/search/phoneNumber/{phoneNumber}")
    public ResponseEntity<CustomerResponse> getCustomerByPhoneNumber(@Valid @PathVariable String phoneNumber) {
        return ResponseEntity.ok(customerService.getCustomerByPhoneNumber(phoneNumber));
    }

    @GetMapping
    public ResponseEntity<List<CustomerResponse>> getAllCustomer() {
        return ResponseEntity.ok(customerService.getAllCustomer());
    }

    @PostMapping
    public ResponseEntity<Void> addCustomer(@Valid @RequestBody CustomerRequest customer) {
        customerService.addCustomer(customer);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateCustomer(@PathVariable Long id, @Valid @RequestBody CustomerRequest customer) {
        customerService.updateCustomer(id, customer);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    //UPDATE Could not commit JPA transaction

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomerById(@PathVariable Long id) {
        customerService.deleteCustomerById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
