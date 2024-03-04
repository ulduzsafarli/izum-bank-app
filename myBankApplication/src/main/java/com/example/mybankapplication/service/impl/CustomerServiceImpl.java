package com.example.mybankapplication.service.impl;

import com.example.mybankapplication.entities.CustomerEntity;
import com.example.mybankapplication.exception.*;
import com.example.mybankapplication.mapper.CustomerMapper;
import com.example.mybankapplication.model.customers.CustomerFilterDto;
import com.example.mybankapplication.model.customers.CustomerResponse;
import com.example.mybankapplication.model.customers.CustomerRequest;
import com.example.mybankapplication.repository.CustomerRepository;
import com.example.mybankapplication.service.CustomerService;
import com.example.mybankapplication.specifications.CustomerSpecifications;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    @Override
    public Page<CustomerResponse> findCustomerByFilter(CustomerFilterDto filterDto, Pageable pageRequest) {
        log.info("Searching customers by filter: {}", filterDto);
        Specification<CustomerEntity> customerSpecification = CustomerSpecifications.getCustomerSpecification(filterDto);
        Page<CustomerEntity> customerEntityPage = customerRepository.findAll(customerSpecification, pageRequest);
        log.info("Successfully found customers");
        return customerEntityPage.map(customerMapper::toDto);
    }

    @Override
    public List<CustomerResponse> getAllCustomer() {
        log.info("Retrieving all customers");
        List<CustomerResponse> customerResponses = customerRepository.findAll().stream()
                .map(customerMapper::toDto).toList();
        log.info("Successfully retrieved all customers");
        return customerResponses;
    }

    @Override
    public CustomerResponse getCustomerById(Long id) {
        log.info("Retrieving customer by ID: {}", id);
        CustomerEntity customerEntity = customerRepository.findById(id)
                .orElseThrow(() -> new NotDataFoundException("Customer not found with ID: " + id));
        CustomerResponse customerResponse = customerMapper.toDto(customerEntity);
        log.info("Successfully retrieved customer with ID: {}", id);
        return customerResponse;
    }

    @Override
    public CustomerResponse getCustomerByEmail(String email) {
        log.info("Retrieving customer by email: {}", email);
        CustomerEntity customerEntity = customerRepository.findByEmail(email)
                .orElseThrow(() -> new NotDataFoundException("Customer not found with email: " + email));
        CustomerResponse customerResponse = customerMapper.toDto(customerEntity);
        log.info("Successfully retrieved customer with email: {}", email);
        return customerResponse;
    }

    @Override
    public CustomerResponse getCustomerByPhoneNumber(String phoneNumber) {
        log.info("Retrieving customer by phone number: {}", phoneNumber);
        CustomerEntity customerEntity = customerRepository.findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new NotDataFoundException("Customer not found with phone number: " + phoneNumber));
        CustomerResponse customerResponse = customerMapper.toDto(customerEntity);
        log.info("Successfully retrieved customer with phone number: {}", phoneNumber);
        return customerResponse;
    }

    @Override
    public void updateCustomer(Long id, CustomerRequest customerRequest) {
        log.info("Updating customer with ID {} to: {}", id, customerRequest);
        CustomerEntity customerEntity = customerRepository.findById(id)
                .orElseThrow(() -> new NotDataFoundException("Customer not found with ID: " + id));
        customerEntity = customerMapper.updateEntityFromRequest(customerRequest, customerEntity);
        customerRepository.save(customerEntity);
        log.info("Successfully updated customer with ID: {}", id);
    }

    @Override
    public void deleteCustomerById(Long id) {
        log.info("Deleting customer by ID: {}", id);
        if (!customerRepository.existsById(id))
            throw new NotDataFoundException("Customer not found with ID: " + id);
        customerRepository.deleteById(id);
        log.info("Successfully deleted customer with ID: {}", id);
    }

    @Override
    public void addCustomer(CustomerRequest customerRequest) {
        log.info("Adding new customer: {}", customerRequest);
        validateNewCustomerData(customerRequest);
        try {
            customerRepository.save(customerMapper.toEntity(customerRequest));
            log.info("Successfully added new customer");
        } catch (DataAccessException ex) {
            throw new DatabaseException("Failed to add new customer to the database", ex);
        }
    }

    private synchronized void validateNewCustomerData(CustomerRequest customerRequest) {
        Optional<CustomerEntity> existingCustomerByEmail = customerRepository.findByEmail(customerRequest.getEmail());
        if (existingCustomerByEmail.isPresent())
            throw new DuplicateDataException("Customer with email " + customerRequest.getEmail() + " already exists");

        Optional<CustomerEntity> existingCustomerByPhoneNumber = customerRepository.findByPhoneNumber(customerRequest.getPhoneNumber());
        if (existingCustomerByPhoneNumber.isPresent())
            throw new DuplicateDataException("Customer with phone number " + customerRequest.getPhoneNumber() + " already exists");
    }

}
