package com.fungisearch.fudriver.customer.command;

import com.fungisearch.fudriver.common.command.CommandResult;
import com.fungisearch.fudriver.customer.command.model.Customer;
import com.fungisearch.fudriver.customer.command.repository.CustomerRepository;
import com.fungisearch.fudriver.testTools.CreateCustomer;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

import static org.junit.Assert.*;



@ContextConfiguration(locations = "/test-spring.xml")
public class UpdateCustomerCommandHandlerITCase extends AbstractTransactionalJUnit4SpringContextTests{

    @Autowired
    private UpdateCustomerCommandHandler handler;
    @Autowired
    private CreateCustomer createCustomer;
    @Autowired
    private CustomerRepository customerRepository;

    @Test
    public void shouldUpdateCustomerNameAndAddress(){
        //Given
        Customer customer = createCustomer.create();

        UpdateCustomerCommand command = new UpdateCustomerCommand();
        command.id = customer.getId();
        command.name = "newName";
        command.address = "newAddress";
        //When
        CommandResult commandResult = handler.handle(command);
        Customer tested = customerRepository.find(customer.getId());
        //Then
        assertEquals(customer.getId(),commandResult.entityId);
        assertEquals(CommandResult.Status.OK,commandResult.status);
        assertEquals("CustomerUpdated", commandResult.body);

        assertEquals(customer.getName(),tested.getName());
        assertEquals(customer.getAddress(),tested.getAddress());
    }
}