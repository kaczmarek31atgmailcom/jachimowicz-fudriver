package com.fungisearch.fudriver.customer;

import com.fungisearch.fudriver.customer.command.DeleteCustomerCommandHandler;
import com.fungisearch.fudriver.customer.command.model.Customer;
import com.fungisearch.fudriver.customer.command.repository.CustomerRepository;
import com.fungisearch.fudriver.testTools.CreateCustomer;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

import static org.junit.Assert.*;


/**
 * Created by marcin on 20.01.17.
 */
@ContextConfiguration(locations = {"/test-spring.xml"})
public class DeleteCustomerCommandHandlerITCase extends AbstractTransactionalJUnit4SpringContextTests {

    @Autowired
    private CreateCustomer createCustomer;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private DeleteCustomerCommandHandler handler;

    @Test
    public void shouldRemoveCustomer(){
        //Given
        Customer customer = createCustomer.create();
        assertTrue(customer.isActive());
        //When
        handler.handle(customer.getId());
        Customer tested = customerRepository.find(customer.getId());
        //Then
        assertFalse(tested.isActive());
    }

}