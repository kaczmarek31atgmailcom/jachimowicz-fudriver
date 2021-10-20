package com.fungisearch.fudriver.customer.command.model;

import com.fungisearch.fudriver.customer.command.repository.CustomerRepository;
import com.fungisearch.fudriver.validation.BeanValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by marcin on 19.01.17.
 */
@Service
public class CustomerFactory {

    private CustomerRepository customerRepository;
    private BeanValidator beanValidator;

    @Autowired
    public CustomerFactory(CustomerRepository customerRepository, BeanValidator beanValidator){
        this.customerRepository = customerRepository;
        this.beanValidator = beanValidator;
    }

    public Customer.CustomerBuilder getBuilder(){
        return new Customer.CustomerBuilder(customerRepository,beanValidator);
    }

    public Customer find(long id){
        Customer customer = customerRepository.find(id);
        if(customer != null){
            customer.customerRepository = customerRepository;
            customer.beanValidator = beanValidator;
        }
    return customer;
    }
}
