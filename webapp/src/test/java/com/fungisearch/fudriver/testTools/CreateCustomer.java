package com.fungisearch.fudriver.testTools;

import com.fungisearch.fudriver.customer.command.model.Customer;
import com.fungisearch.fudriver.customer.command.repository.CustomerRepository;
import com.fungisearch.fudriver.validation.BeanValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by marcin on 20.01.17.
 */
@Service
public class CreateCustomer {

    private final CustomerRepository customerRepository;
    private final BeanValidator beanValidator;

    @Autowired
    public CreateCustomer(CustomerRepository customerRepository, BeanValidator beanValidator){
        this.customerRepository = customerRepository;
        this.beanValidator = beanValidator;
    }

    public Customer create(){
        Customer customer = new Customer
                .CustomerBuilder(customerRepository,beanValidator)
                .name("test_customer")
                .address("test_address")
                .build();
        customer.create();
        return customer;
    }
}
