package com.fungisearch.fudriver.customer.command.repository;

import com.fungisearch.fudriver.customer.command.model.Customer;

/**
 * Created by marcin on 19.01.17.
 */
public interface CustomerRepository {

    void save(Customer customer);
    Customer find(long id);
}
