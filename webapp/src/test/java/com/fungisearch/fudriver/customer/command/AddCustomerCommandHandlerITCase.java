package com.fungisearch.fudriver.customer.command;

import com.fungisearch.fudriver.customer.command.model.Customer;
import com.fungisearch.fudriver.customer.command.repository.CustomerRepository;
import com.fungisearch.fudriver.validation.BeanValidator;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;



/**
 * Created by marcin on 19.01.17.
 */
@ContextConfiguration(locations = {"/test-spring.xml"})
public class AddCustomerCommandHandlerITCase extends AbstractTransactionalJUnit4SpringContextTests {

    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private BeanValidator beanValidator;
    @Autowired
    private AddCustomerCommandHandler addCustomerCommandHandler;


    @Test
    public void shouldCreateCustomer() throws Exception {
        //Given
        AddCustomerCommand addCustomerCommand = new AddCustomerCommand();
        addCustomerCommand.name = "Marcin Kaczmarek";
        addCustomerCommand.address = "Garwolin Dobra 5/10";
        //When
        long customerId = addCustomerCommandHandler.handle(addCustomerCommand).entityId;
        Customer tested = customerRepository.find(customerId);
        //Then
        Assert.assertEquals(addCustomerCommand.name,tested.getName());
        Assert.assertEquals(addCustomerCommand.address,tested.getAddress());
    }
}