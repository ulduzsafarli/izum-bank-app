package com.example.mybankapplication.service;

import com.example.mybankapplication.model.customers.CustomerFilterDto;
import com.example.mybankapplication.model.customers.CustomerRequest;
import com.example.mybankapplication.model.customers.CustomerResponse;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CustomerService {
    Page<CustomerResponse> findCustomerByFilter(CustomerFilterDto filterDto, Pageable pageRequest);
    List<CustomerResponse> getAllCustomer();
    CustomerResponse getCustomerById(Long id);
    CustomerResponse getCustomerByEmail(String email);
    CustomerResponse getCustomerByPhoneNumber(String phoneNumber);
    @Transactional
    void updateCustomer(Long id, CustomerRequest customer);
    @Transactional
    void deleteCustomerById(Long id);
    @Transactional
    void addCustomer(CustomerRequest customer);

}
