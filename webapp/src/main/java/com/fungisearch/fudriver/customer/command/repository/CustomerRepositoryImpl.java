package com.fungisearch.fudriver.customer.command.repository;

import com.fungisearch.fudriver.customer.command.model.Customer;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Created by marcin on 19.01.17.
 */
@Repository
public class CustomerRepositoryImpl implements CustomerRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    public void save(Customer customer) {
        em.persist(customer);
    }

    @Override
    public Customer find(long id) {
        return em.find(Customer.class, id);
    }
}
